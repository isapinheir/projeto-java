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
        alterar.classList.add('oculto');
        container.classList.remove('blur');
        inputId.value = '';
    }
}

function ocultar(){
    alterar.classList.add('oculto');
    container.classList.remove('blur');
    inputId.value = '';
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

document.getElementById('ocultar').addEventListener('click', () =>{popupAlterar()});