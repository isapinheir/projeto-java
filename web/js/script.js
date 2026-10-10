async function obterProjetos(){
    try {
        const resposta = await fetch('http://localhost:7000/projetos');
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
                        <span class="icone-status"></span>${projeto.status}
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
        console.log("bloquear a barra de busca")
    }
});

window.onload = async () => {
    const projetos = await obterProjetos();
    if(projetos){
        tabela(projetos);
    }
};