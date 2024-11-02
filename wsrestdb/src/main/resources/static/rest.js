async function asyncLerImoveis(proxSucesso, proxErro) {
    const URL = `/api/imoveis`;
    fetch(URL)
        .then(resposta => {
            if (!resposta.ok) throw new Error(resposta.status);
            return resposta.json();
        })
        .then(jsonResponse => proxSucesso(jsonResponse))
        .catch(proxErro);
}