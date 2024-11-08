const imoveisList = document.getElementById('imoveisList');
const formAdicionarImovel = document.getElementById('formAdicionarImovel');
const logoutButton = document.getElementById('logout');
const formLogin = document.getElementById('formLogin');
const formRegistro = document.getElementById('formRegistro');
const mensagemLogin = document.getElementById('mensagemLogin');
const mensagemRegistro = document.getElementById('mensagemRegistro');
const searchButton = document.getElementById('searchButton');
const searchInput = document.getElementById('searchImovel');
const formEditarImovel = document.getElementById('formEditarImovel');
const editModal = document.getElementById('editModal'); // Modal de edição

async function carregarImoveis(query = '') {
    try {
        const response = await fetch(`/api/imoveis${query ? `?search=${encodeURIComponent(query)}` : ''}`);
        if (!response.ok) {
            throw new Error(`Erro na requisição: ${response.statusText}`);
        }
        const imoveis = await response.json();
        if (Array.isArray(imoveis)) {
            imoveisList.innerHTML = '';
            if (imoveis.length === 0) {
                imoveisList.innerHTML = '<li>Nenhum imóvel encontrado.</li>';
            }
            imoveis.forEach(imovel => {
                const li = document.createElement('li');
                li.classList.add('imovel');
                li.innerHTML = `
                    <img src="${imovel.foto}" alt="${imovel.titulo}">
                    <div class="imovel-details">
                        <strong>${imovel.titulo}</strong><br>
                        <span>${imovel.descricao}</span><br>
                        <span>R$ ${imovel.preco}</span><br>
                        <span><em>${imovel.tipo === 'venda' ? 'Para Venda' : 'Para Aluguel'}</em></span><br>
                        <span>${imovel.rua}, ${imovel.numero} - ${imovel.cidade}, ${imovel.estado} - CEP: ${imovel.cep}</span>
                    </div>
                    <button onclick="editarImovel(${imovel.id})">Editar</button>
                    <button onclick="excluirImovel(${imovel.id})">Excluir</button>
                `;
                imoveisList.appendChild(li);
            });
        } else {
            throw new Error('Erro: A resposta não é uma lista de imóveis.');
        }
    } catch (error) {
        console.error('Erro ao carregar imóveis:', error);
        alert(`Erro ao carregar imóveis: ${error.message}`);
    }
}

window.onload = () => {
    if (!verificarUsuarioLogado()) {
        window.location.href = 'index.html';
    } else {
        carregarImoveis();
    }
};

function verificarUsuarioLogado() {
    const usuarioLogado = localStorage.getItem('usuarioLogado');
    if (usuarioLogado) {
        document.getElementById('userDisplayName').textContent = usuarioLogado;
        return true;
    }
    return false;
}

if (formAdicionarImovel) {
    formAdicionarImovel.onsubmit = async (event) => {
        event.preventDefault();
        const imovelData = obterDadosImovel();
        try {
            const response = await fetch('/api/imoveis', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(imovelData),
            });

            if (response.ok) {
                carregarImoveis();
                formAdicionarImovel.reset();
                formAdicionarImovel.style.display = 'none';
            } else {
                const errorText = await response.text();
                alert('Erro ao adicionar imóvel: ' + errorText);
            }
        } catch (error) {
            console.error('Erro ao adicionar imóvel:', error);
            alert('Erro ao adicionar imóvel: ' + error.message);
        }
    };
}

if (searchButton) {
    searchButton.onclick = () => {
        const query = searchInput.value.trim();
        carregarImoveis(query);
    };
}

async function editarImovel(id) {
    try {
        const response = await fetch(`/api/imoveis/${id}`);
        const imovel = await response.json();

        if (imovel) {
            preencherFormulario(imovel);

            editModal.style.display = 'block';

            formEditarImovel.onsubmit = async (event) => {
                event.preventDefault();

                const imovelData = obterDadosImovel();

                const responseUpdate = await fetch(`/api/imoveis/${id}`, {
                    method: 'PUT',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(imovelData),
                });

                if (responseUpdate.ok) {
                    carregarImoveis();

                    editModal.style.display = 'none';
                } else {
                    alert('Erro ao editar imóvel');
                }
            };
        } else {
            alert('Erro ao carregar imóvel para edição');
        }
    } catch (error) {
        console.error('Erro ao editar imóvel:', error);
        alert('Erro ao carregar imóvel para edição');
    }
}

async function excluirImovel(id) {
    if (confirm('Você tem certeza que deseja excluir este imóvel?')) {
        try {
            const response = await fetch(`/api/imoveis/${id}`, {
                method: 'DELETE',
            });
            if (response.ok) {
                carregarImoveis();
            } else {
                alert('Erro ao excluir imóvel');
            }
        } catch (error) {
            console.error('Erro ao excluir imóvel:', error);
            alert('Erro ao excluir imóvel');
        }
    }
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
        mensagemLogin.textContent = '';
        const username = document.getElementById('usernameLogin').value;
        const password = document.getElementById('passwordLogin').value;

        try {
            const response = await fetch('/api/usuarios/login', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ username, password }),
            });

            const message = await response.text();
            mensagemLogin.textContent = message;
            if (response.ok) {
                localStorage.setItem('usuarioLogado', username);
                window.location.href = '/imoveis.html';
            }
        } catch (error) {
            console.error('Erro no login:', error);
            mensagemLogin.textContent = 'Erro ao realizar login.';
        }
    };
}

if (formRegistro) {
    formRegistro.onsubmit = async (event) => {
        event.preventDefault();
        mensagemRegistro.textContent = '';
        const username = document.getElementById('usernameRegistro').value;
        const password = document.getElementById('passwordRegistro').value;

        try {
            const response = await fetch('/api/usuarios', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ username, password }),
            });

            const message = await response.text();
            mensagemRegistro.textContent = message; // Mostra mensagem da resposta

            if (response.ok) {
                formRegistro.reset();
            }
        } catch (error) {
            console.error('Erro no registro:', error);
            mensagemRegistro.textContent = 'Erro ao registrar usuário.';
        }
    };
}

function obterDadosImovel() {
    return {
        titulo: document.getElementById('tituloImovel').value,
        descricao: document.getElementById('descricaoImovel').value,
        preco: document.getElementById('precoImovel').value,
        foto: document.getElementById('fotoImovel').value,
        tipo: document.getElementById('tipoImovel').value,
        rua: document.getElementById('ruaImovel').value,
        numero: document.getElementById('numeroImovel').value,
        cidade: document.getElementById('cidadeImovel').value,
        estado: document.getElementById('estadoImovel').value,
        cep: document.getElementById('cepImovel').value
    };
}

function preencherFormulario(imovel) {
    document.getElementById('tituloImovel').value = imovel.titulo;
    document.getElementById('descricaoImovel').value = imovel.descricao;
    document.getElementById('precoImovel').value = imovel.preco;
    document.getElementById('fotoImovel').value = imovel.foto;
    document.getElementById('tipoImovel').value = imovel.tipo;
    document.getElementById('ruaImovel').value = imovel.rua;
    document.getElementById('numeroImovel').value = imovel.numero;
    document.getElementById('cidadeImovel').value = imovel.cidade;
    document.getElementById('estadoImovel').value = imovel.estado;
    document.getElementById('cepImovel').value = imovel.cep;
}

if (editModal) {
    editModal.onclick = (event) => {
        if (event.target === editModal) {
            editModal.style.display = 'none';
        }
    };
}