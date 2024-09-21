package uz.pdp.controller;

import jakarta.validation.Valid;
import lombok.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uz.pdp.exceptions.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Controller
@RequestMapping("/book")
public class BookController {
    private final AtomicInteger counter = new AtomicInteger(1);
    private final List<Book> books = new ArrayList<>() {{
        add(new Book(counter.getAndIncrement(), "Reactive Spring", "Josh Long"));
        add(new Book(counter.getAndIncrement(), "Spring Security With Easy Way", "Laura Spilca"));
    }};

    @GetMapping("")
    public String books(Model model) {
        model.addAttribute("books", books);
        return "books";
    }

    @GetMapping("/add")
    public String createPage(Model model) {
        model.addAttribute("dto", new BookCreateDTO());
        return "create";
    }

    @PostMapping("/add")
    public String create(@Valid @ModelAttribute("dto") BookCreateDTO dto, BindingResult errors) {
        if (errors.hasErrors()) {
            System.out.println(errors);
            return "create";
        }
        if (!dto.getTitle().equals(dto.getAuthor())) {
            errors.rejectValue("title", "", "Tile must match to Author");
            errors.rejectValue("author", "", "Author must match to Title");
            return "create";
        }
        books.add(new Book(counter.getAndIncrement(), dto.getTitle(), dto.getAuthor()));
        return "redirect:/book";
    }

    @GetMapping("/delete/{id}")
    public String deletePage(Model model, @PathVariable Integer id) {
//        Optional<Book> optionalBook = books.stream().filter(book -> book.getId().equals(id)).findFirst();
//        if (optionalBook.isPresent()) {
//            model.addAttribute("books", optionalBook.get());
//        } else {
//            model.addAttribute("error", "Book Not Found");
//        }
        Book book = books.stream()
                .filter(b -> b.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Book not found with id: '%s'".formatted(id), "/book"));
        model.addAttribute("books", book);
        return "delete";
    }

    @GetMapping("/login/page")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login/page")
    public String login(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error_message","Bad Credentials");
        return "redirect:/book/login/page";
    }

//    @PostMapping("/delete/{id}")
//    public String delete(Model model, @PathVariable String id) {
//        Book book = books.get(Integer.parseInt(id));
//        books.remove(book);
//        return "redirect:/book";
//    }


//    @ExceptionHandler({NotFoundException.class})
//    public ModelAndView error_404(HttpServletRequest request, NotFoundException e) {
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.setViewName("error/404");
//        modelAndView.addObject("message", e.getMessage());
//        modelAndView.addObject("path", request.getRequestURI());
//        modelAndView.addObject("back_path", e.getPath());
//        return modelAndView;
//    }
}

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
class Book {
    private Integer id;
    private String title;
    private String author;
}
