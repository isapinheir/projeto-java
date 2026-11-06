import { obterProjetos } from "./api.js";

const projetos = await obterProjetos();
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

async function atualizarGraficoRosquinha() {
    let total = projetos.length;
    const concluido = projetos.filter(p => p.status.toLowerCase().includes('concluído')).length;
    const desenvolvimento = projetos.filter(p => p.status.toLowerCase().includes('desenvolvimento')).length;
    const planejado = projetos.filter(p => p.status.toLowerCase().includes('planejado')).length;

    // evita a divisão por 0, causando NaN a aparecer na tela
    if(total == 0){
        total = 1;
    }

    let p1 = (concluido / total) * 100;
    let p2 = (desenvolvimento / total) * 100;
    let p3 = (planejado / total) * 100;

    const slice1 = document.getElementById('slice1');
    const slice2 = document.getElementById('slice2');
    const slice3 = document.getElementById('slice3');

    slice1.setAttribute('stroke-dasharray', `${p1} ${100 - p1}`);
    slice1.setAttribute('stroke-dashoffset', 25);

    slice2.setAttribute('stroke-dasharray', `${p2} ${100 - p2}`);
    // O offset subtrai o valor do primeiro status para empurrar o traço para frente
    slice2.setAttribute('stroke-dashoffset', `${25 - p1}`);

    slice3.setAttribute('stroke-dasharray', `${p3} ${100 - p3}`);
    slice3.setAttribute('stroke-dashoffset', `${25 - (p1 + p2)}`);

    document.getElementById('porc-legenda1').textContent = `${Math.round(p1)}%`;
    document.getElementById('porc-legenda2').textContent = `${Math.round(p2)}%`;
    document.getElementById('porc-legenda3').textContent = `${Math.round(p3)}%`;

}
atualizarGraficoRosquinha();

async function atualizarGraficoBarras(){
    let total = projetos.length;

    const software = projetos.filter(p => p.categoria.toLowerCase().includes('software')).length;
    const web = projetos.filter(p => p.categoria.toLowerCase().includes('web')).length;
    const mobile = projetos.filter(p => p.categoria.toLowerCase().includes('mobile')).length;

    // evita a divisão por 0, causando NaN a aparecer na tela
    if(total == 0){
        total = 1;
    }

    let porcSw = (software / total) * 100;
    let porcWeb = (web / total) * 100;
    let porcMob = (mobile / total) * 100;

    let [sw1, sw2] = String(porcSw).split('');
    let [web1, web2] = String(porcWeb).split('');
    let [mob1, mob2] = String(porcMob).split('');


    graficoSw.style.height = `${sw1}.${sw2}rem`;
    legendaGraficoSw.textContent = software;

    graficoWeb.style.height = `${web1}.${web2}rem`;
    legendaGraficoWeb.textContent = web;

    graficoMob.style.height = `${mob1}.${mob2}rem`;
    legendaGraficoMob.textContent = mobile;

}
atualizarGraficoBarras();

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
    ocultarPopupAlterar()
});

document.getElementById('grafico-rosquinha').addEventListener('mouseover', (event) =>{
    event.target.setAttribute('stroke-width', 8);
});
document.getElementById('grafico-rosquinha').addEventListener('mouseout', (event) =>{
    event.target.setAttribute('stroke-width', 6);
});

// === WINDOW ===

window.paginacao = paginacao;
window.popupAlterar = popupAlterar;