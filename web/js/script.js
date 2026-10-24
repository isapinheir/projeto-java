
document.addEventListener('DOMContentLoaded', () => {

    // Elementos DOM
    const btnLimpar = document.getElementById('btnLimpar');
    const btnExcluir = document.getElementById('btnExcluir');
    const formProjeto = document.getElementById('formProjeto');
    const campoBusca = document.getElementById('campoBusca');
    const tabelaProjetos = document.getElementById('tabelaProjetos');
    const linhasTabela = tabelaProjetos.querySelectorAll('tbody tr');
    const btnAtualizar = document.getElementById('btnAtualizar');

    // 1. Ação do Botão Limpar Formulário
    btnLimpar.addEventListener('click', () => {
        document.getElementById('nomeProjeto').value = '';
        document.getElementById('descricaoProjeto').value = '';
        document.getElementById('categoriaProjeto').selectedIndex = 0;
        document.getElementById('statusProjeto').selectedIndex = 0;
        document.getElementById('nomeProjeto').focus();
    });

    // 2. Ação do Botão Excluir
    btnExcluir.addEventListener('click', () => {
        if (confirm('Deseja realmente excluir este projeto?')) {
            btnLimpar.click();
        }
    });

    // 3. Filtro de Pesquisa na Tabela em Tempo Real
    campoBusca.addEventListener('keyup', () => {
        const termo = campoBusca.value.toLowerCase().trim();

        linhasTabela.forEach(linha => {
            const textoLinha = linha.textContent.toLowerCase();
            if (textoLinha.includes(termo)) {
                linha.style.display = '';
            } else {
                linha.style.display = 'none';
            }
        });
    });

    // 4. Ação do Botão Atualizar Pesquisa
    btnAtualizar.addEventListener('click', () => {
        campoBusca.value = '';
        linhasTabela.forEach(linha => linha.style.display = '');
    });

    // 5. Troca interativa de itens do menu sidebar
    const menuItens = document.querySelectorAll('.sidebar-item');
    menuItens.forEach(item => {
        item.addEventListener('click', (e) => {
            menuItens.forEach(i => i.classList.remove('ativo'));
            item.classList.add('ativo');
        });
    });
});

function atualizarRelogio() {
    const agora = new Date();

    const dataAtual = agora.toLocaleDateString('pt-BR', {
        weekday: 'long'
    });

    const diaAtual = agora.toLocaleDateString('pt-BR', {
        day: 'numeric',
        month: 'long',
        year: 'numeric',
    });

    const horas = String(agora.getHours()).padStart(2, '0');
    const minutos = String(agora.getMinutes()).padStart(2, '0');
    const horarioAtual = `${horas}:${minutos}`;

    document.getElementById('dia').textContent = dataAtual.charAt(0).toUpperCase()+dataAtual.slice(1);
    document.getElementById('horario').textContent = diaAtual + ", " + horarioAtual;
}
atualizarRelogio();
setInterval(atualizarRelogio, 10000);

function telaAlterar(id){
    const container = document.getElementById('painel-corpo');
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

/*
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
});*/