const imoveisList = document.getElementById('imoveisList');
const formAdicionarImovel = document.getElementById('formAdicionarImovel');
const logoutButton = document.getElementById('logout');
const formLogin = document.getElementById('formLogin');
const formRegistro = document.getElementById('formRegistro');
const mensagemLogin = document.getElementById('mensagemLogin');
const mensagemRegistro = document.getElementById('mensagemRegistro');

async function carregarImoveis() {
    const response = await fetch('/api/imoveis');
    const imoveis = await response.json();

    if (Array.isArray(imoveis)) {
        imoveisList.innerHTML = ''; // Limpa a lista existente
        imoveis.forEach(imovel => {
            const li = document.createElement('li');
            li.classList.add('imovel');
            li.innerHTML = `
                <img src="${imovel.foto}" alt="${imovel.titulo}">
                <div class="imovel-details">
                    <strong>${imovel.titulo}</strong><br>
                    <span>${imovel.descricao}</span><br>
                    <span>R$ ${imovel.preco}</span><br>
                    <span><em>${imovel.tipo === 'venda' ? 'Para Venda' : 'Para Aluguel'}</em></span>
                </div>
            `;
            imoveisList.appendChild(li);
        });
    } else {
        console.error('Erro: A resposta não é uma lista de imóveis.');
    }
}

window.onload = () => {
    if (formAdicionarImovel) {
        carregarImoveis();
        if (!localStorage.getItem('usuarioLogado')) {
            window.location.href = 'index.html';
        }
    }

    if (formLogin) {
        if (localStorage.getItem('usuarioLogado')) {
            window.location.href = '/imoveis.html';
        }
    }
};

if (formAdicionarImovel) {
    formAdicionarImovel.onsubmit = async (event) => {
        event.preventDefault();
        const titulo = document.getElementById('tituloImovel').value;
        const descricao = document.getElementById('descricaoImovel').value;
        const preco = document.getElementById('precoImovel').value;
        const foto = document.getElementById('fotoImovel').value;
        const tipo = document.getElementById('tipoImovel').value;

        const response = await fetch('/api/imoveis', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ titulo, descricao, preco, foto, tipo }),
        });

        if (response.ok) {
            carregarImoveis();
            formAdicionarImovel.reset();
        } else {
            const errorText = await response.text();
            alert('Erro ao adicionar imóvel: ' + errorText);
        };
    };
}

if (logoutButton) {
    logoutButton.onclick = () => {
        localStorage.removeItem('usuarioLogado');
        window.location.href = 'index.html';
    };
}

if (formLogin) {
    formLogin.onsubmit = async (event) => {
        event.preventDefault();
        mensagemLogin.textContent = ''; // Limpa mensagem anterior
        const username = document.getElementById('usernameLogin').value;
        const password = document.getElementById('passwordLogin').value;

        const response = await fetch('/api/usuarios/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ username, password }),
        });

        const message = await response.text();
        mensagemLogin.textContent = message; // Mostra mensagem da resposta
        if (response.ok) {
            localStorage.setItem('usuarioLogado', username);
            window.location.href = '/imoveis.html';
        }
    };
}

if (formRegistro) {
    formRegistro.onsubmit = async (event) => {
        event.preventDefault();
        mensagemRegistro.textContent = ''; // Limpa mensagem anterior
        const username = document.getElementById('usernameRegistro').value;
        const password = document.getElementById('passwordRegistro').value;

        const response = await fetch('/api/usuarios', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ username, password }),
        });

        const message = await response.text();
        mensagemRegistro.textContent = message; // Mostra mensagem da resposta

        if (response.ok) {
            formRegistro.reset();
        }
    };
}