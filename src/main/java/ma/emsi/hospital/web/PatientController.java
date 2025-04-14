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
public class PatientController {

    @Autowired
    private PatientRepository patientRepository;
    @GetMapping("/index")
    public String index(Model model,
                        @RequestParam(name="page", defaultValue = "0") int page,
                        @RequestParam(name="size", defaultValue = "4") int size,
                        @RequestParam(name="keyword", defaultValue = "") String kw

    ) {
        Page<Patient> pagepatients = patientRepository.findByNomContains(kw, PageRequest.of(page, size));
        model.addAttribute("listPatients", pagepatients.getContent());
        model.addAttribute("pages", new int[pagepatients.getTotalPages()]);
        model.addAttribute("currentPage", page);
        model.addAttribute("keyword", kw);
        return "patient";
    }
    @GetMapping("/delete")
    public String delete(@RequestParam(name="id") Long id,@RequestParam(name="keyword", defaultValue = "")
                         String keyword,
                         @RequestParam(name="page", defaultValue = "0")  int page){

        patientRepository.deleteById(id);
        return "redirect:/index?page="+page+"&keyword="+keyword;
    }

    @GetMapping("/")
    public String home(){
        return "redirect:/index";
    }
    @GetMapping("/formPatients")
    public String formPatient(Model model){

        model.addAttribute("patient", new Patient());
        return "formPatients";
    }

    @PostMapping("/save")
    public String save(Model model,  Patient patient, BindingResult bindingResult,
                       @RequestParam(defaultValue = "0") int page ,
                       @RequestParam(defaultValue = "") String keyword){
        if(bindingResult.hasErrors()){ return "formPatients";}
        patientRepository.save(patient);
        return "redirect:/index?page="+page+"&keyword="+keyword;
    }
    @GetMapping("/editPatient")
    public String editPatient(Model model, @RequestParam(name="id") Long id,String keyword , int page) {
        Patient patient = patientRepository.findById(id).orElse(null);
        if(patient == null) {
            throw new RuntimeException("Patient introuvable");
        }
        model.addAttribute("patient", patient);
        model.addAttribute("keyword", keyword);
        model.addAttribute("page", page);
        return "editPatient";
    }

    @GetMapping("/index/{id}")
    public String viewPatient(@PathVariable("id") Long id, Model model){
        Patient patient = patientRepository.findById(id).get();
        model.addAttribute("patient", patient);
        return "viewPatient";
    }


}