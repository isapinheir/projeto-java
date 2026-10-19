const API_URL = 'http://localhost:7000/projetos'
const form = document.getElementById('formProjeto');

async function obterProjetos(){
    try {
        const resposta = await fetch(API_URL);
        console.log(resposta);
        if (!resposta.ok) {
            throw new Error('Erro ao buscar dados da API');
        }
        const projetos = await resposta.json();
        return projetos
    }
    catch (error) {
        console.error('Erro:', error);
        alert('Não foi possível obter os dados dos projetos.');
    }
}

async function idAtual(){
    try {
        const projetos = obterProjetos();
        let maior = 0;
        projetos.forEach((projeto) => {
            if(projeto.id > maior){
                maior = projeto.id;
                console.log(maior);
            }
        });
        return maior;
    }
    catch (error) {
        console.error('Erro:', error);
        alert('Erro ao cadastrar o projeto.');
    }
}

async function listarProjetos(){
    const projetos = await obterProjetos();
    if(projetos){
        tabela(projetos);
    }
}

async function cadastrarProjeto(e) {
    e.preventDefault();
    id = (await idAtual() + 1);

    const projeto = {
        id: id,
        nome: document.getElementById('nome').value,
        descricao: document.getElementById('descricao').value,
        categoria: document.getElementById('categoria').value,
        status: document.getElementById('status').value,
    }

    try{
        const resposta = await fetch(API_URL,{
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify(projeto)
        });

        if(!resposta.ok){
            throw new Error("Erro ao cadastrar o projeto.");
        }

        alert("Projeto cadastrado com sucesso.");
        form.reset();
        listarProjetos();
    }
    catch(err){
        alert("Não foi possível cadastrar o projeto.");
        console.error(err);
    }
    
}

async function excluirProjeto(id){
    const confirmar = confirm("Deseja realmente excluir esse projeto?");
    if(!confirmar){
        return;
    }

    try{
        const resposta = await fetch(`${API_URL}/${id}`,{
            method: "DELETE"
        });

        if(!resposta.ok){
            throw new Error("Erro ao excluir o projeto.");
        }
        
        alert("Projeto excluído com sucesso.");
        await listarProjetos();
    }
    catch(e){
        alert("Não foi possível excluir o projeto.");
        console.error(e);
    }
}

async function alterarProjeto(e){
    e.preventDefault();

    const id = document.getElementById('inputId').value;
    const projeto = {
        id: id,
        nome: document.getElementById('nomeNovo').value,
        descricao: document.getElementById('descricaoNova').value,
        categoria: document.getElementById('categoriaNova').value,
        status: document.getElementById('statusNovo').value,
    }

    const resposta = await fetch(`${API_URL}/${id}`,{
        method: "PUT",
        headers:{"Content-Type": "application/json"},
        body: JSON.stringify(projeto),
    });
    if(!resposta.ok){
        throw new Error("Erro ao alterar o projeto.");
    }
    telaAlterar();
    listarProjetos();
    return await resposta.json();
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
        
        const iEditar = document.createElement('i');
        iEditar.className = 'fa-solid fa-pencil';
        iEditar.addEventListener('click', () => telaAlterar(projeto.id));
        
        const iExcluir = document.createElement('i');
        iExcluir.className = 'fa-solid fa-trash';
        iExcluir.addEventListener('click', () => excluirProjeto(projeto.id));

        tdAcoes.appendChild(iEditar);
        tdAcoes.appendChild(iExcluir);
        tr.appendChild(tdAcoes);

        tbody.appendChild(tr);
    });
}

function telaAlterar(id){
    const container = document.getElementById('container');
    const alterar = document.getElementById('alterar');
    const fechado = alterar.classList.contains('oculto');
    const inputId = document.getElementById('inputId');

    if (fechado){
        alterar.classList.remove('oculto');
        container.classList.add('blur');
        inputId.value = id;
    } 
    else{
        alterar.classList.add('oculto');
        container.classList.remove('blur');
        inputId.value = '';
    }
}

document.getElementById('buscaInput').addEventListener('input', async (e) => {
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
});

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

document.getElementById('ocultar').addEventListener('click', () =>{telaAlterar()});

form.addEventListener('submit', async (e) =>{cadastrarProjeto(e)});
formAlterar.addEventListener('submit', async (e) =>{alterarProjeto(e)});

window.onload = listarProjetos();