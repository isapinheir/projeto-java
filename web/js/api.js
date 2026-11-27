const API_URL = 'http://localhost:7000';

let projetos = [];
let usuario = lerUsuarioSalvo();

/* ---------- sessão (somente no navegador) ---------- */

function lerUsuarioSalvo() {
    try { return sessionStorage.getItem('usuario'); } catch { return null; }
}

function salvarUsuario(nome) {
    try {
        if (nome) sessionStorage.setItem('usuario', nome);
        else sessionStorage.removeItem('usuario');
    } catch { /* armazenamento indisponível */ }
}

export function getUsuario() {
    return usuario;
}

export function sair() {
    log("LOGOUT", usuario, null, `${API_URL}/api/logs/sessao`);
    usuario = null;
    projetos = [];
    salvarUsuario(null);
}

/* ---------- autenticação ---------- */

export function validarSenha(senha) {
    return /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[^A-Za-z0-9]).{8,}$/.test(senha);
}

export async function autenticarUsuario(usuarioDigitado, senhaDigitada) {
    let resposta;
    try {
        resposta = await fetch(`${API_URL}/login`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            // ATENÇÃO: confira se o servidor espera exatamente estes nomes de campos
            body: JSON.stringify({
                usuario: usuarioDigitado,
                senha: senhaDigitada
            })
        });
    } catch (err) {
        log("ERRO", "servidor_indisponivel", err, `${API_URL}/api/logs/erro`);
        throw new Error("Não foi possível conectar ao servidor.");
    }

    if (resposta.status === 401) {
        log("LOGIN", "login_nao_autorizado", null, `${API_URL}/api/logs/sessao`);
        throw new Error("Usuário ou senha inválidos.");
    }
    if (!resposta.ok) {
        log("ERRO", "falha_autenticacao", `${resposta.status}`, `${API_URL}/api/logs/erro`);
        throw new Error("Erro ao realizar login. Tente novamente.");
    }

    usuario = usuarioDigitado;
    salvarUsuario(usuario);
    log("LOGIN", usuario, null, `${API_URL}/api/logs/sessao`);
    return usuario;
}

export async function cadastrarUsuario(novoUsuario, senha) {
    if (!validarSenha(senha)) {
        throw new Error("A senha não atende aos requisitos.");
    }

    let resposta;
    try {
        resposta = await fetch(`${API_URL}/cadastro`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ usuario: novoUsuario, senha })
        });
    } catch (err) {
        log("ERRO", "servidor_indisponivel", err, `${API_URL}/api/logs/erro`);
        throw new Error("Não foi possível conectar ao servidor.");
    }

    if (resposta.status === 409) {
        throw new Error("Esse usuário já está cadastrado.");
    }
    if (!resposta.ok) {
        log("ERRO", "falha_cadastro", `${resposta.status}`, `${API_URL}/api/logs/erro`);
        throw new Error("Não foi possível realizar o cadastro.");
    }

    log("CADASTRO", novoUsuario, null, `${API_URL}/api/logs/sessao`);
    return novoUsuario;
}

/* ---------- projetos ---------- */

export async function obterProjetos() {
    try {
        const resposta = await fetch(`${API_URL}/projetos`);
        if (!resposta.ok) {
            log("ERRO", "API", `${resposta.status}`, `${API_URL}/api/logs/erro`);
            throw new Error('Erro ao buscar dados da API');
        }
        return await resposta.json();
    }
    catch (error) {
        console.error('Erro:', error);
        log("ERRO", "API", error, `${API_URL}/api/logs/erro`);
        alert('Não foi possível obter os dados dos projetos.');
        return [];
    }
}

function proximoId() {
    return projetos.reduce((maior, p) => Math.max(maior, Number(p.id) || 0), 0) + 1;
}

export async function listarProjetos() {
    if (!usuario) return;               // só carrega depois do login
    projetos = await obterProjetos();
    tabela(projetos);
    categorias(projetos);
    metricas();
}

async function cadastrarProjeto(e) {
    e.preventDefault();
    const form = e.target;
    try {
        projetos = await obterProjetos();   // dados atualizados para calcular o id
        const projeto = {
            id: proximoId(),
            nome: document.getElementById('nome').value,
            descricao: document.getElementById('descricao').value,
            categoria: document.getElementById('categoria').value,
            status: document.getElementById('status').value,
        };

        const resposta = await fetch(`${API_URL}/projetos`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(projeto)
        });

        if (!resposta.ok) {
            log("ERRO", "cadastro_projeto", `${resposta.status}`, `${API_URL}/api/logs/erro`);
            throw new Error("Erro ao cadastrar o projeto.");
        }

        alert("Projeto cadastrado com sucesso.");
        log("CRIAR", `ID ${projeto.id} | NOME ${projeto.nome}`, null, `${API_URL}/api/logs/projeto`);
        form.reset();
        await listarProjetos();
    }
    catch (err) {
        alert("Não foi possível cadastrar o projeto.");
        log("ERRO", "cadastro_projeto", err, `${API_URL}/api/logs/erro`);
        console.error(err);
    }
}

async function excluirProjeto(id) {
    if (!confirm("Deseja realmente excluir esse projeto?")) return;

    try {
        const resposta = await fetch(`${API_URL}/projetos/${id}`, { method: "DELETE" });

        if (!resposta.ok) {
            log("ERRO", "exclusao_projeto", `${resposta.status}`, `${API_URL}/api/logs/erro`);
            throw new Error("Erro ao excluir o projeto.");
        }
        log("EXCLUIR", `ID ${id}`, null, `${API_URL}/api/logs/projeto`);
        alert("Projeto excluído com sucesso.");
        await listarProjetos();
    }
    catch (e) {
        alert("Não foi possível excluir o projeto.");
        log("ERRO", "exclusao_projeto", e, `${API_URL}/api/logs/erro`);
        console.error(e);
    }
}

function popupAlterar(id) {
    const projeto = projetos.find(p => String(p.id) === String(id));
    if (!projeto) return;

    document.getElementById('inputId').value = projeto.id;
    document.getElementById('nomeNovo').value = projeto.nome;
    document.getElementById('descricaoNova').value = projeto.descricao;
    document.getElementById('categoriaNova').value = projeto.categoria;
    document.getElementById('statusNovo').value = projeto.status;

    document.getElementById('alterar').classList.remove('oculto');
    document.getElementById('painel-corpo').classList.add('blur');
}

function fecharAlterar() {
    document.getElementById('alterar').classList.add('oculto');
    document.getElementById('painel-corpo').classList.remove('blur');
    document.getElementById('inputId').value = '';
}

async function alterarProjeto(e) {
    e.preventDefault();

    try {
        const id = document.getElementById('inputId').value;
        const projeto = {
            id: id,
            nome: document.getElementById('nomeNovo').value,
            descricao: document.getElementById('descricaoNova').value,
            categoria: document.getElementById('categoriaNova').value,
            status: document.getElementById('statusNovo').value,
        };

        const resposta = await fetch(`${API_URL}/projetos/${id}`, {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(projeto),
        });
        if (!resposta.ok) {
            log("ERRO", "alteracao_projetos", `${resposta.status}`, `${API_URL}/api/logs/erro`);
            throw new Error("Erro no servidor do projeto.");
        }
        log("ALTERAR", `ID ${id}`, null, `${API_URL}/api/logs/projeto`);
        const atualizado = await resposta.json();
        fecharAlterar();
        await listarProjetos();
        return atualizado;
    }
    catch (e) {
        console.error("Erro: " + e);
        log("ERRO", "alteracao_projetos", e, `${API_URL}/api/logs/erro`);
        alert("Não foi possível alterar o projeto.");
    }
}

/* ---------- renderização ---------- */

function normalizarStatus(status) {
    return status
        ? status.normalize('NFD').replace(/[\u0300-\u036f]/g, "").toLowerCase()
        : '';
}

function criarBadgeStatus(status) {
    const badge = document.createElement('span');
    badge.className = `badge ${normalizarStatus(status)}`;

    const icone = document.createElement('span');
    icone.className = 'icone-status';

    const p = document.createElement('p');
    p.textContent = status;

    badge.appendChild(icone);
    badge.appendChild(p);
    return badge;
}

function tabela(data) {
    const tbody = document.getElementById('tBody');
    if (!tbody) return;                 // componente ainda não carregado
    tbody.innerHTML = '';

    if (data.length === 0) {
        tbody.innerHTML = `<tr><td colspan="7" class="nome-projeto">Nenhum projeto encontrado.</td></tr>`;
        return;
    }

    data.forEach((projeto) => {
        const tr = document.createElement('tr');

        const tdNome = document.createElement('td');
        const pNome = document.createElement('p');
        pNome.className = 'nome-projeto';
        pNome.textContent = projeto.nome;
        tdNome.appendChild(pNome);
        tr.appendChild(tdNome);

        const tdDesc = document.createElement('td');
        const pDesc = document.createElement('p');
        pDesc.textContent = projeto.descricao;
        tdDesc.appendChild(pDesc);
        tr.appendChild(tdDesc);

        const tdCat = document.createElement('td');
        const spanCat = document.createElement('span');
        spanCat.className = 'badge categoria';
        spanCat.textContent = projeto.categoria;
        tdCat.appendChild(spanCat);
        tr.appendChild(tdCat);

        const tdStts = document.createElement('td');
        tdStts.appendChild(criarBadgeStatus(projeto.status));
        tr.appendChild(tdStts);

        const tdAcoes = document.createElement('td');

        const iEditar = document.createElement('ion-icon');
        iEditar.setAttribute('name', 'pencil');
        iEditar.addEventListener('click', () => popupAlterar(projeto.id));

        const iExcluir = document.createElement('ion-icon');
        iExcluir.setAttribute('name', 'trash');
        iExcluir.addEventListener('click', () => excluirProjeto(projeto.id));

        tdAcoes.appendChild(iEditar);
        tdAcoes.appendChild(iExcluir);
        tr.appendChild(tdAcoes);

        tbody.appendChild(tr);
    });
}

function categorias(data) {
    const grupos = {
        software: 'tbodySoftware',
        web: 'tbodyWeb',
        mobile: 'tbodyMobile',
    };

    for (const [chave, idTbody] of Object.entries(grupos)) {
        const tbody = document.getElementById(idTbody);
        if (!tbody) continue;
        tbody.innerHTML = '';           // evita duplicar linhas a cada atualização

        data
            .filter(p => (p.categoria || '').toLowerCase().includes(chave))
            .forEach((projeto) => {
                const tr = document.createElement('tr');

                const tdNome = document.createElement('td');
                const pNome = document.createElement('p');
                pNome.className = 'nome-projeto';
                pNome.textContent = projeto.nome;
                tdNome.appendChild(pNome);
                tr.appendChild(tdNome);

                const tdStts = document.createElement('td');
                tdStts.appendChild(criarBadgeStatus(projeto.status));
                tr.appendChild(tdStts);

                tbody.appendChild(tr);
            });
    }
}

function metricas() {
    const contar = (trecho) =>
        projetos.filter(p => normalizarStatus(p.status).includes(trecho)).length;

    const definir = (id, valor) => {
        const el = document.getElementById(id);
        if (el) el.textContent = valor;
    };

    definir('metrica-total', projetos.length);
    definir('metrica-concluido', contar('concluido'));
    definir('metrica-desenvolvimento', contar('desenvolvimento'));
    definir('metrica-planejado', contar('planejado'));
}

function busca(e) {
    const termo = e.target.value.toLowerCase();
    const filtro = projetos.filter(p =>
        [p.nome, p.descricao, p.categoria, p.status]
            .some(campo => (campo || '').toLowerCase().includes(termo))
    );
    tabela(filtro);
}

/* ---------- eventos ---------- */

// Evita registrar o mesmo listener várias vezes (o evento
// "componenteCarregado" dispara uma vez por componente).
function ligar(el, evento, fn) {
    if (!el || el.dataset.ligado === evento) return;
    el.dataset.ligado = evento;
    el.addEventListener(evento, fn);
}

function inicializarEventos(root = document) {
    ligar(root.querySelector('#formProjeto'), 'submit', cadastrarProjeto);
    ligar(root.querySelector('#formAlterar'), 'submit', alterarProjeto);
    ligar(root.querySelector('#ocultar'), 'click', fecharAlterar);
    ligar(root.querySelector('#btnAtualizar'), 'click', () => listarProjetos());
    ligar(root.querySelector('#campoBusca'), 'input', busca);
}

document.addEventListener('componenteCarregado', () => {
    inicializarEventos();
    listarProjetos();
});
inicializarEventos();   // caso os componentes já tenham sido carregados

/* ---------- logs ---------- */

async function log(acao, detalhes, erro, rota) {
    try {
        const corpo = {
            origem: "WEB",
            acao: acao,
            detalhes: detalhes,
            erro: erro ? String(erro) : null,
        };
        const resposta = await fetch(rota, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(corpo)
        });

        if (!resposta.ok) {
            throw new Error(`Erro de log(${acao}) = ${resposta.status}`);
        }
    }
    catch (err) {
        console.error(err);
    }
}

document.addEventListener('visibilitychange', () => {
    if (!usuario) return;               // só registra sessão de quem está logado

    const acao = document.visibilityState === 'visible' ? "LOGIN" : "LOGOUT";
    const payload = new Blob(
        [JSON.stringify({ origem: "WEB", acao, detalhes: usuario, erro: null })],
        { type: "application/json" }    // string pura iria como text/plain
    );
    navigator.sendBeacon(`${API_URL}/api/logs/sessao`, payload);
});