import { obterProjetos } from "./api.js";

const projetos = await obterProjetos();

// === INFO PROJETOS ===

let total = projetos.length;

const software = projetos.filter(p => p.categoria.toLowerCase().includes('software')).length;
let porcSw = (software / total) * 100;

const web = projetos.filter(p => p.categoria.toLowerCase().includes('web')).length;
let porcWeb = (web / total) * 100;

const mobile = projetos.filter(p => p.categoria.toLowerCase().includes('mobile')).length;
let porcMob = (mobile / total) * 100;

const concluido = projetos.filter(p => p.status.toLowerCase().includes('concluído')).length;
const desenvolvimento = projetos.filter(p => p.status.toLowerCase().includes('desenvolvimento')).length;
const planejado = projetos.filter(p => p.status.toLowerCase().includes('planejado')).length;

let naoConcluido = total - concluido;
let porcNaoConcluido = (naoConcluido / total) * 100;

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

async function atualizarGraficoRosquinha() {
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
    // evita a divisão por 0, causando NaN a aparecer na tela
    if(total == 0){
        total = 1;
    }

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

async function atualizarDadosDashboard(){
    dashboardTotal.textContent = total;
    dashboardNaoConcluido.textContent = `${naoConcluido} (${Math.round(porcNaoConcluido)}%)`;
    dashboardSw.textContent = `${Math.round(porcSw)}%`;
    dashboardWeb.textContent = `${Math.round(porcWeb)}%`;
    dashboardMob.textContent = `${Math.round(porcMob)}%`;
}
atualizarDadosDashboard();

// === EVENT LISTENERS ===

document.addEventListener('componenteCarregado', (e) => {
  inicializarEventos();
});

function inicializarEventos(){;
    const btnLimpar = document.getElementById('btnLimpar');
    const btnOcultar = document.getElementById('ocultar');
    const menuItens = document.querySelectorAll('.sidebar-item');
    const graficoRosquinha = document.getElementById('grafico-rosquinha');

    if(btnLimpar){
        btnLimpar.addEventListener('click', () => {
        document.getElementById('nome').value = '';
        document.getElementById('descricao').value = '';
        document.getElementById('categoria').selectedIndex = 0;
        document.getElementById('status').selectedIndex = 0;
        document.getElementById('nome').focus();
        });
    }

    if(menuItens){
        // sidebar
        menuItens.forEach(item => {
            item.addEventListener('click', (e) => {
                menuItens.forEach(i => i.classList.remove('ativo'));
                item.classList.add('ativo');
            });
        });
    }

    if(btnOcultar){
        btnOcultar.addEventListener('click', () =>{
            ocultarPopupAlterar()
        });
    }

    if(graficoRosquinha){
        graficoRosquinha.addEventListener('mouseover', (event) =>{
            event.target.setAttribute('stroke-width', 8);
        });
        graficoRosquinha.addEventListener('mouseout', (event) =>{
            event.target.setAttribute('stroke-width', 6);
        });
    }
}