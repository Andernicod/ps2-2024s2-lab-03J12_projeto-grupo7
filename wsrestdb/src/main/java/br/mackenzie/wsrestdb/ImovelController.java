package br.mackenzie.wsrestdb;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/imoveis") // Define o prefixo para as rotas deste controlador
public class ImovelController {

    @GetMapping
    public String consultarTodosImoveis(HttpSession session) {
        // Verifica se o usuário está logado
        if (session.getAttribute("usuarioLogado") == null) {
            return "Acesso negado. Você precisa estar logado para acessar esta página.";
        }
        return "Acesso permitido. Listagem de imóveis aqui."; // Aqui você poderia retornar a lista de imóveis.
    }
}
