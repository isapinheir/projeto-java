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

async function listarProjetos(){
    const projetos = await obterProjetos();
    if(projetos){
        tabela(projetos);
    }
}

async function excluirProjeto(){
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

async function alterarProjeto(id, projeto){
    const resposta = await fetch()(`${API_URL}/${id}`,{
        method: "PUT",
        headers:{"Content-Type": "application/json"},
        body: JSON.stringify(projeto),
    });
    if(!resposta.ok){
        throw new Error("Erro ao alterar o projeto.");
    }
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
        
        tr.innerHTML = `
            
            <tr>
                <td>
                    <p class="nome-projeto">${projeto.nome}</p>
                </td>
                <td>
                    <p>${projeto.descricao}</p>
                </td>
                <td>
                    <span class="badge categoria">${projeto.categoria}</span>
                </td>
                <td>
                    <span class="badge ${projeto.status ? projeto.status.normalize('NFD').replace(/[\u0300-\u036f]/g, "").toLowerCase() : ''}">
                        <span class="icone-status"></span><p>${projeto.status}</p>
                    </span>
                </td>
            </tr>
        `;
        tbody.appendChild(tr);
    });
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
    const popup = document.getElementById('popup');

    if(projetos.style.display === 'flex'){
        projetos.style.display = 'none';
        popup.style.display = 'block';
        btnAdd.classList.add('ativado');
    }
    else{
        projetos.style.display = 'flex';
        popup.style.display = 'none';
        btnAdd.classList.remove('ativado');
    }
})

form.addEventListener('submit', async (e) =>{
    e.preventDefault();

    const projeto = {
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
});

window.onload = listarProjetos();