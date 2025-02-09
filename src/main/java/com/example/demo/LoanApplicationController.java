package com.example.demo;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoanApplicationController {

    @Autowired
    private LoanRepository loanRepository;

    @GetMapping("/apply")
    public String showForm(Model model) {
        model.addAttribute("loanApplication", new LoanApplication());
        return "apply";
    }

    @PostMapping("/submit")
    public String processApplication(@Valid @ModelAttribute LoanApplication loanApplication,
                                     BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "apply";
        }

        // Loan approval logic
        if (loanApplication.getCreditScore() >= 700) {
            loanApplication.setStatus("Approved");
        } else if (loanApplication.getCreditScore() < 500) {
            loanApplication.setStatus("Rejected");
        } else {
            loanApplication.setStatus("Pending");
        }

        loanRepository.save(loanApplication);
        return "redirect:/status/" + loanApplication.getId();
    }

    @GetMapping("/status/{id}")
    public String viewStatus(@PathVariable Long id, Model model) {
        LoanApplication loanApplication = loanRepository.findById(id).orElse(null);
        model.addAttribute("loanApplication", loanApplication);
        return "status";
    }
}
