const API_URL = 'http://localhost:7000';
const usuario = "Carlos Alberto";
log("LOGIN", usuario, null, `${API_URL}/api/logs/sessao`);

export async function obterProjetos(){
    try {
        const resposta = await fetch(`${API_URL}/projetos`);
        if (!resposta.ok) {
            log("ERRO", "API", `${resposta.status}`, `${API_URL}/api/logs/erro`);
            throw new Error('Erro ao buscar dados da API');
        }
        const projetos = await resposta.json();
        return projetos
    }
    catch (error) {
        console.error('Erro:', error);
        log("ERRO", "API", error, `${API_URL}/api/logs/erro`);
        alert('Não foi possível obter os dados dos projetos.');
    }
}

async function idAtual(){
    try {
        const projetos = await obterProjetos();
        let maior = 0;
        projetos.forEach((projeto) => {
            if(projeto.id > maior){
                maior = projeto.id;
            }
        });
        return maior;
    }
    catch (error) {
        console.error('Erro:', error);
        log("ERRO", "obter_id", error, `${API_URL}/api/logs/erro`);
        return error;
    }
}

async function listarProjetos(){
    const projetos = await obterProjetos();
    if(projetos){
        tabela(projetos);
        categorias(projetos);
    }
}

async function cadastrarProjeto(e) {
    e.preventDefault();
    try{
        let id = (await idAtual() + 1);

        const projeto = {
            id: id,
            nome: document.getElementById('nome').value,
            descricao: document.getElementById('descricao').value,
            categoria: document.getElementById('categoria').value,
            status: document.getElementById('status').value,
        }
    
        const resposta = await fetch(`${API_URL}/projetos`,{
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify(projeto)
        });

        if(!resposta.ok){
            log("ERRO", "cadastro_projeto", `${resposta.status}`, `${API_URL}/api/logs/erro`);
            throw new Error("Erro ao cadastrar o projeto.");
        }

        alert("Projeto cadastrado com sucesso.");
        log("CRIAR", `ID ${id} | NOME ${nome}`, null, `${API_URL}/api/logs/projeto`);
        formProjeto.reset();
        listarProjetos();
    }
    catch(err){
        alert("Não foi possível cadastrar o projeto.");
        log("ERRO", "cadastro_projeto", err, `${API_URL}/api/logs/erro`);
        console.error(err);
    }
    
}

async function excluirProjeto(id){
    const confirmar = confirm("Deseja realmente excluir esse projeto?");
    if(!confirmar){
        return;
    }

    try{
        const resposta = await fetch(`${API_URL}/projetos/${id}`,{
            method: "DELETE"
        });

        if(!resposta.ok){
            log("ERRO", "exclusao_projeto", `${resposta.status}`, `${API_URL}/api/logs/erro`);
            throw new Error("Erro ao excluir o projeto.");
        }
        log("EXCLUIR", `ID ${id}`, null, `${API_URL}/api/logs/projeto`);
        alert("Projeto excluído com sucesso.");
        await listarProjetos();
    }
    catch(e){
        alert("Não foi possível excluir o projeto.");
        log("ERRO", "API", `${resposta.status}`, `${API_URL}/api/logs/erro`);
        console.error(e);
    }
}

async function alterarProjeto(e){
    e.preventDefault();

    try{
        const id = document.getElementById('inputId').value;
        const projeto = {
            id: id,
            nome: document.getElementById('nomeNovo').value,
            descricao: document.getElementById('descricaoNova').value,
            categoria: document.getElementById('categoriaNova').value,
            status: document.getElementById('statusNovo').value,
        }

        const resposta = await fetch(`${API_URL}/projetos/${id}`,{
            method: "PUT",
            headers:{"Content-Type": "application/json"},
            body: JSON.stringify(projeto),
        });
        if(!resposta.ok){
            log("ERRO", "alteracao_projetos", `${resposta.status}`, `${API_URL}/api/logs/erro`);
            throw new Error("Erro no servidor do projeto.");
        }
        log("ALTERAR", `ID ${id}`, null, `${API_URL}/api/logs/projeto`);
        document.getElementById('alterar').classList.add('oculto');
        document.getElementById('painel-corpo').classList.remove('blur');
        document.getElementById('inputId').value = '';
        listarProjetos();
        return await resposta.json();
    }
    catch(e){
      console.error("Erro: " + e);
      log("ERRO", "API", e, `${API_URL}/api/logs/erro`);
      alert("Não foi possível alterar o projeto."); 
    }
}

function tabela(data) {
    const tbody = document.getElementById('tBody');
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
        const sttsBadge = document.createElement('span');
        
        const statusNormalizado = projeto.status ? projeto.status.normalize('NFD').replace(/[\u0300-\u036f]/g, "").toLowerCase() : '';
        
        sttsBadge.className = `badge ${statusNormalizado}`;

        const spanIcone = document.createElement('span');
        spanIcone.className = 'icone-status';

        const pStatus = document.createElement('p');
        pStatus.textContent = projeto.status;

        sttsBadge.appendChild(spanIcone);
        sttsBadge.appendChild(pStatus);
        tdStts.appendChild(sttsBadge);
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

function categorias(data){
    const tbSw = document.getElementById('tbodySoftware');
    const projSw = data.filter(sw => sw.categoria.toLowerCase().includes('software'));

    const tbWeb = document.getElementById('tbodyWeb');
    const projWeb = data.filter(sw => sw.categoria.toLowerCase().includes('web'));

    const tbMob = document.getElementById('tbodyMobile');
    const projMob = data.filter(sw => sw.categoria.toLowerCase().includes('mobile'));

    projSw.forEach((projeto) => {
        const tr = document.createElement('tr');
        const tdNome = document.createElement('td');
        const pNome = document.createElement('p');
        pNome.className = 'nome-projeto';
        pNome.textContent = projeto.nome;
        tdNome.appendChild(pNome);
        tr.appendChild(tdNome);

        const tdStts = document.createElement('td');
        const sttsBadge = document.createElement('span');
        
        const statusNormalizado = projeto.status ? projeto.status.normalize('NFD').replace(/[\u0300-\u036f]/g, "").toLowerCase() : '';
        
        sttsBadge.className = `badge ${statusNormalizado}`;

        const spanIcone = document.createElement('span');
        spanIcone.className = 'icone-status';

        const pStatus = document.createElement('p');
        pStatus.textContent = projeto.status;

        sttsBadge.appendChild(spanIcone);
        sttsBadge.appendChild(pStatus);
        tdStts.appendChild(sttsBadge);
        tr.appendChild(tdStts);
        tbSw.appendChild(tr);
    });

    projWeb.forEach((projeto) => {
        const tr = document.createElement('tr');
        const tdNome = document.createElement('td');
        const pNome = document.createElement('p');
        pNome.className = 'nome-projeto';
        pNome.textContent = projeto.nome;
        tdNome.appendChild(pNome);
        tr.appendChild(tdNome);

        const tdStts = document.createElement('td');
        const sttsBadge = document.createElement('span');
        
        const statusNormalizado = projeto.status ? projeto.status.normalize('NFD').replace(/[\u0300-\u036f]/g, "").toLowerCase() : '';
        
        sttsBadge.className = `badge ${statusNormalizado}`;

        const spanIcone = document.createElement('span');
        spanIcone.className = 'icone-status';

        const pStatus = document.createElement('p');
        pStatus.textContent = projeto.status;

        sttsBadge.appendChild(spanIcone);
        sttsBadge.appendChild(pStatus);
        tdStts.appendChild(sttsBadge);
        tr.appendChild(tdStts);
        tbWeb.appendChild(tr);
    });

    projMob.forEach((projeto) => {
        const tr = document.createElement('tr');
        const tdNome = document.createElement('td');
        const pNome = document.createElement('p');
        pNome.className = 'nome-projeto';
        pNome.textContent = projeto.nome;
        tdNome.appendChild(pNome);
        tr.appendChild(tdNome);

        const tdStts = document.createElement('td');
        const sttsBadge = document.createElement('span');
        
        const statusNormalizado = projeto.status ? projeto.status.normalize('NFD').replace(/[\u0300-\u036f]/g, "").toLowerCase() : '';
        
        sttsBadge.className = `badge ${statusNormalizado}`;

        const spanIcone = document.createElement('span');
        spanIcone.className = 'icone-status';

        const pStatus = document.createElement('p');
        pStatus.textContent = projeto.status;

        sttsBadge.appendChild(spanIcone);
        sttsBadge.appendChild(pStatus);
        tdStts.appendChild(sttsBadge);
        tr.appendChild(tdStts);
        tbMob.appendChild(tr);
    }); 
}

async function metricas(){
    const projetos = await obterProjetos();

    const total = projetos.length;
    const concluido = projetos.filter(p => p.status.toLowerCase().includes('concluído'));
    const desenvolvimento = projetos.filter(p => p.status.toLowerCase().includes('desenvolvimento'));
    const planejado = projetos.filter(p => p.status.toLowerCase().includes('planejado'));

    document.getElementById('metrica-total').textContent = (total);
    document.getElementById('metrica-concluido').textContent = concluido.length;
    document.getElementById('metrica-desenvolvimento').textContent = desenvolvimento.length;
    document.getElementById('metrica-planejado').textContent = planejado.length;
}

async function busca (e){
    const dados = await obterProjetos();
    if(dados){
        const projetos = Object.values(dados);
        const busca = e.target.value.toLowerCase();
        const filtro = projetos.filter(p => 
            p.nome.toLowerCase().includes(busca) || 
            p.descricao.toLowerCase().includes(busca) ||
            p.categoria.toLowerCase().includes(busca) ||
            p.status.toLowerCase().includes(busca)
        );
        tabela(filtro);
    }
    else{
        buscaInput.style.cursor = 'not-allowed';
    }
}




/*
document.getElementById('btnAdd').addEventListener('click',() =>{
    const projetos = document.getElementById('projetos');
    const cadastro = document.getElementById('cadastro');

    if(projetos.style.display === 'flex'){
        projetos.style.display = 'none';
        cadastro.style.display = 'block';
        btnAdd.classList.add('ativado');
    }
    else{
        projetos.style.display = 'flex';
        cadastro.style.display = 'none';
        btnAdd.classList.remove('ativado');
    }
})
*/
 function inicializarEventos(root = document) {
  const formProjeto = root.querySelector('#formProjeto');
  const formAlterar = root.querySelector('#formAlterar');
  const btnAtualizar = root.querySelector('#btnAtualizar');
  const campoBusca = root.querySelector('#campoBusca');

  if (formProjeto) {
    formProjeto.addEventListener('submit', async (e) => {
      await cadastrarProjeto(e);
    });
  }

  if (formAlterar) {
    formAlterar.addEventListener('submit', async (e) => {
      await alterarProjeto(e);
    });
  }

  if (btnAtualizar) {
    btnAtualizar.addEventListener('click', () => {
      listarProjetos();
    });
  }

  if (campoBusca) {
    campoBusca.addEventListener('input', async (e) => {
      await busca(e);
    });
  }
}
document.addEventListener('componenteCarregado', () => {
  inicializarEventos();
  listarProjetos();
  metricas();
});

async function log(acao, detalhes, erro, rota) {
    try{
        const log = {
            origem: "WEB",
            acao: acao,
            detalhes: detalhes,
            erro: erro,
        }
        const resposta = await fetch(rota,{
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify(log)
        });

        if(!resposta.ok){
            throw new Error(`Erro de log(${acao}) = ${resposta.status}`);
        }
    }
    catch(err){
        console.error(err);
    }
}

window.onload = listarProjetos();
document.addEventListener('visibilitychange', () => {
    let acao;
    if (document.visibilityState === 'visible') {
        acao = "LOGIN";
    }
    if (document.visibilityState === 'hidden') {
        acao = "LOGOUT";
    }
    const payload = JSON.stringify({
        origem: "WEB",
        acao: acao,
        detalhes: usuario,
        erro: null,
    });
    navigator.sendBeacon(`${API_URL}/api/logs/sessao`, payload);
    }
);