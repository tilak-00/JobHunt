package com.example.jobhunt.controller;

import com.example.jobhunt.dto.SignupRequest;
import com.example.jobhunt.entity.User;
import com.example.jobhunt.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    // ------------------ LOGIN ------------------
    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "logout", required = false) String logout,
                            Model model) {
        if (error != null) model.addAttribute("error", "❌ Invalid email or password.");
        if (logout != null) model.addAttribute("message", "✅ Logged out successfully.");
        return "login";
    }



    // ------------------ SIGNUP ------------------
    @GetMapping("/signup")
    public String signupPage() {
        return "signup";
    }

    @PostMapping("/signup")
    public void processSignup(@ModelAttribute SignupRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain");

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("❌ Passwords do not match.");
            return;
        }

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("❌ Email already registered.");
            return;
        }

        if (userRepository.findByMobileNumber(request.getMobileNumber()).isPresent()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("❌ Mobile number already registered.");
            return;
        }

        // ✅ Create and save user with encoded password
        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setMobileNumber(request.getMobileNumber());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // << password encrypted
        user.setProvider("LOCAL");

        userRepository.save(user);

        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write("✅ Signup successful.");
    }

    // ------------------ LOGOUT ------------------
    @GetMapping("/logout-success")
    public String logoutSuccess(HttpServletResponse response, HttpSession session) {
        session.invalidate();
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        return "redirect:/home1";  // ✅ redirect to public page
    }


    // ------------------ STATIC PAGES ------------------
    @GetMapping("/internships")
    public String internshipsPage() {
        return "internships";
    }

    @GetMapping("/jobs")
    public String jobsPage() {
        return "jobs";
    }

    @GetMapping("/about")
    public String aboutPage() {
        return "about";
    }

    @GetMapping("/contact")
    public String contactPage() {
        return "contact";
    }

    @GetMapping("/adminLGN")
    public String adminLoginPage() {
        return "adminLGN";
    }

    @GetMapping("/admin/DashbrdADMN")
    public String adminDashboardPage() {
        return "DashbrdADMN"; // must match your template file name
    }


    @GetMapping("/admin/add-job")
    public String addJob() {
        return "add_job";
    }

    @GetMapping("/admin/add-intern")
    public String addIntern() {
        return "add_intern";
    }
    



    // ------------------ HOME REDIRECT ------------------
    @GetMapping("/")
    public String homeRedirect() {
        return "redirect:/home1";  // ✅ Redirect to public home1 page
    }


    @GetMapping("/home")
    public String homePage() {
        return "home"; // Make sure this view exists
    }
    @GetMapping("/home1")
    public String home1Page() {
        return "home1"; // This must match templates/home1.html
    }

}
