const alterar = document.getElementById('alterar');
const fechado = alterar.classList.contains('oculto');
const inputId = document.getElementById('inputId');
const container = document.getElementById('painel-corpo');

async function carregarComponente() {
    const db = await fetch("./dashboard.html");
    const dashboard = await db.text();
    document.getElementById('paginaDashboard').innerHTML = dashboard;

    const pj = await fetch("./projetos.html");
    const projetos = await pj.text();
    document.getElementById('paginaProjetos').innerHTML = projetos;

    const cat = await fetch("./categorias.html");
    const categorias = await cat.text();
    document.getElementById('paginaCategorias').innerHTML = categorias;

    document.dispatchEvent(new CustomEvent('componenteCarregado'));
}

carregarComponente();

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

function paginacao(pag){
    ocultarPopupAlterar();
    switch (pag) {
        case 1:
            ocultarProj();
            ocultarCat();
            paginaDashboard.classList.remove('oculto');
            break;
        case 2:
            ocultarDashboard();
            ocultarCat();
            paginaProjetos.classList.remove('oculto');
            break;
        case 3:
            ocultarDashboard();
            ocultarProj();
            paginaCategorias.classList.remove('oculto');
            break;
    }
}
// === WINDOW ===
window.paginacao = paginacao;
window.popupAlterar = popupAlterar;