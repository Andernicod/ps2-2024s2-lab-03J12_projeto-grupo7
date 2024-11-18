package br.mackenzie.webapp.Imovel;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.server.ResponseStatusException;
import br.mackenzie.webapp.security.dao.UserDao;
import br.mackenzie.webapp.security.model.DAOUser;

@RestController
@RequestMapping("/api/imoveis")
public class ImovelController {

    @Autowired
    private ImovelRepo imovelRepo;

    @Autowired
    private UserDao userDao;
    @GetMapping
    public Iterable<Imovel> getImoveis(@RequestParam Optional<String> search) {
        if (search.isPresent()) {
            return imovelRepo.findByRuaContainingIgnoreCase(search.get());
        } else {
            return imovelRepo.findAll();
        }
    }

    @GetMapping("/{id}")
    public Optional<Imovel> getImovel(@PathVariable long id) {
        return imovelRepo.findById(id);
    }

    @PostMapping
    public Imovel createImovel(@RequestBody Imovel imovel) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        DAOUser usuario = userDao.findByUsername(username);
        if (usuario == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado.");
        }
        imovel.setUsuario(usuario);
        return imovelRepo.save(imovel);
    }

    @PutMapping("/{imovelId}")
    public Imovel updateImovel(@RequestBody Imovel imovelRequest, @PathVariable long imovelId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Optional<Imovel> existingImovel = imovelRepo.findById(imovelId);
        if (existingImovel.isPresent()) {
            Imovel imovel = existingImovel.get();
            if (!imovel.getUsuario().getUsername().equals(username)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não tem permissão para editar este imóvel.");
            }
            imovel.setTitulo(imovelRequest.getTitulo());
            imovel.setDescricao(imovelRequest.getDescricao());
            imovel.setPreco(imovelRequest.getPreco());
            imovel.setFoto(imovelRequest.getFoto());
            imovel.setTipo(imovelRequest.getTipo());
            imovel.setRua(imovelRequest.getRua());
            imovel.setNumero(imovelRequest.getNumero());
            imovel.setCidade(imovelRequest.getCidade());
            imovel.setEstado(imovelRequest.getEstado());
            imovel.setCep(imovelRequest.getCep());
            return imovelRepo.save(imovel);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Imóvel não encontrado.");
    }

    @DeleteMapping("/{id}")
    public void deleteImovel(@PathVariable long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Optional<Imovel> imovelOptional = imovelRepo.findById(id);
        if (imovelOptional.isPresent()) {
            Imovel imovel = imovelOptional.get();
            if (!imovel.getUsuario().getUsername().equals(username)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não tem permissão para excluir este imóvel.");
            }
            imovelRepo.delete(imovel);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Imóvel não encontrado.");
        }
    }
}