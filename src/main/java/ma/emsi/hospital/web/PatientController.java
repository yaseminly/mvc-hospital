package ma.emsi.hospital.web;

import lombok.AllArgsConstructor;
import ma.emsi.hospital.entities.Patient;
import ma.emsi.hospital.repository.PatientRepository;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@AllArgsConstructor
public class PatientController {

    private PatientRepository patientRepository;

    @GetMapping("/index")
    public String index(Model model,
                        @RequestParam(value = "page",defaultValue = "0") int p
            ,@RequestParam(value = "size",defaultValue = "4") int s
            ,@RequestParam(value = "keyword",defaultValue = "") String kw) {
        Page<Patient> pagePatients = patientRepository.findByNomContains(kw,PageRequest.of(p,s));
        model.addAttribute("listPatients", pagePatients.getContent());
        model.addAttribute("pages",new int[pagePatients.getTotalPages()]);
        model.addAttribute("currentPage",p);
        model.addAttribute("keyword",kw);
        return "patients";
    }
    @GetMapping("/delete")
    public String delete(@RequestParam(name ="id") Long id,
                         @RequestParam(name ="keyword" ,defaultValue = "") String keyword,
                         @RequestParam(name ="page" ,defaultValue = "0") String page ) {
        patientRepository.deleteById(id);
        return "redirect:/index?page="+page+"&keyword="+keyword;
    }
    @GetMapping("/")
    public String home() {
        return "redirect:/index";
    }

}