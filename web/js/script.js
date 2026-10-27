const container = document.getElementById('painel-corpo');
const alterar = document.getElementById('alterar');
const fechado = alterar.classList.contains('oculto');
const inputId = document.getElementById('inputId');
const btnLimpar = document.getElementById('btnLimpar');
const menuItens = document.querySelectorAll('.sidebar-item');

// === FUNÇÕES ===

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

function popupAlterar(id){
    if (fechado){
        alterar.classList.remove('oculto');
        container.classList.add('blur');
        inputId.value = id;
    } 
    else{
        ocultarPopupAlterar();
    }
}

function ocultarPopupAlterar(){
    alterar.classList.add('oculto');
    container.classList.remove('blur');
    inputId.value = '';
}

function ocultarDashboard(){
    paginaDashboard.classList.add('oculto');
}

function ocultarProj(){
    paginaProjetos.classList.add('oculto');
}

function ocultarCat(){
   paginaCategorias.classList.add('oculto'); 
}

function ocultarRel(){
    paginaRelatorios.classList.add('oculto');
}

function ocultarConfig(){
    paginaConfiguracoes.classList.add('oculto');
}

function paginacao(pag){
    ocultarPopupAlterar();
    switch (pag) {
        case 1:
            ocultarProj();
            ocultarCat();
            ocultarRel();
            ocultarConfig();
            paginaDashboard.classList.remove('oculto');
            break;
        case 2:
            ocultarDashboard();
            ocultarCat();
            ocultarRel();
            ocultarConfig();
            paginaProjetos.classList.remove('oculto');
            break;
        case 3:
            ocultarDashboard();
            ocultarProj();
            ocultarRel();
            ocultarConfig();
            paginaCategorias.classList.remove('oculto');
            break;
        case 4:
            ocultarDashboard();
            ocultarProj();
            ocultarCat();
            ocultarConfig();
            paginaRelatorios.classList.remove('oculto');
            break;
        case 5:
            ocultarDashboard();
            ocultarProj();
            ocultarCat();
            ocultarRel();
            paginaConfiguracoes.classList.remove('oculto');
            break;
        default:
            break;
    }
}

// === EVENT LISTENERS ===

btnLimpar.addEventListener('click', () => {
    document.getElementById('nome').value = '';
    document.getElementById('descricao').value = '';
    document.getElementById('categoria').selectedIndex = 0;
    document.getElementById('status').selectedIndex = 0;
    document.getElementById('nome').focus();
});
// sidebar
menuItens.forEach(item => {
    item.addEventListener('click', (e) => {
        menuItens.forEach(i => i.classList.remove('ativo'));
        item.classList.add('ativo');
    });
});

document.getElementById('ocultar').addEventListener('click', () =>{
    popupAlterar()
});

document.getElementById('grafico-rosquinha').addEventListener('mouseover', (event) =>{
    event.target.setAttribute('stroke-width', 8);
});
document.getElementById('grafico-rosquinha').addEventListener('mouseout', (event) =>{
    event.target.setAttribute('stroke-width', 6);
});