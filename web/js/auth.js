import {
    autenticarUsuario,
    cadastrarUsuario,
    getUsuario,
    listarProjetos,
    validarSenha,
} from './api.js';

const $ = (id) => document.getElementById(id);

const telaLogin = $('telaLogin');
const telaCadastro = $('telaCadastro');
const erroLogin = $('erroLogin');
const erroCadastro = $('erroCadastro');

function mostrarErro(elemento, mensagem) {
    elemento.textContent = mensagem || '';
    elemento.classList.toggle('oculto', !mensagem);
}

function irParaCadastro() {
    mostrarErro(erroLogin, '');
    telaLogin.classList.add('oculto');
    telaCadastro.classList.remove('oculto');
}

function irParaLogin() {
    mostrarErro(erroCadastro, '');
    telaCadastro.classList.add('oculto');
    telaLogin.classList.remove('oculto');
}

function entrarNoSistema() {
    telaLogin.classList.add('oculto');
    telaCadastro.classList.add('oculto');
    document.querySelector('.sidebar').classList.remove('oculto');
    document.querySelector('.conteudo-principal').classList.remove('oculto');
    $('nomeUsuario').textContent = getUsuario();
    listarProjetos();
}

$('btnIrCadastro').addEventListener('click', irParaCadastro);
$('btnIrLogin').addEventListener('click', irParaLogin);

$('formLogin').addEventListener('submit', async (e) => {
    e.preventDefault();
    mostrarErro(erroLogin, '');

    const botao = $('btnLogin');
    botao.disabled = true;

    try {
        await autenticarUsuario($('loginEmail').value.trim(), $('loginSenha').value);
        $('formLogin').reset();
        entrarNoSistema();
    } catch (err) {
        mostrarErro(erroLogin, err.message);
    } finally {
        botao.disabled = false;
    }
});

$('formCadastro').addEventListener('submit', async (e) => {
    e.preventDefault();
    mostrarErro(erroCadastro, '');

    const novoUsuario = $('cadastrarEmail').value.trim();
    const senha = $('cadastrarSenha').value;

    if (!validarSenha(senha)) {
        mostrarErro(erroCadastro, 'A senha não atende aos requisitos listados acima.');
        return;
    }

    const botao = $('btnCadastro');
    botao.disabled = true;

    try {
        await cadastrarUsuario(novoUsuario, senha);
        $('formCadastro').reset();
        alert('Cadastro realizado com sucesso! Faça login para continuar.');
        $('loginEmail').value = novoUsuario;
        irParaLogin();
    } catch (err) {
        mostrarErro(erroCadastro, err.message);
    } finally {
        botao.disabled = false;
    }
});

// Mantém o usuário logado ao recarregar a página (sessionStorage)
if (getUsuario()) {
    entrarNoSistema();
}