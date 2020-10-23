package com.example.demo.Controller;

import com.example.demo.model.Product;
import com.example.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
@RequestMapping("product")
public class ProductControllers {
    @Autowired
    private ProductService productService;

    private Page<Product> search(Optional<String> s, Pageable pageInfo) {
        return productService.search(s.get(), pageInfo);
    }

    private Page<Product> getPage(Pageable pageInfo) {
        return productService.findAll(pageInfo);
    }

    @GetMapping
    public ModelAndView showList(Optional<String> s, Pageable pageInfo) {
        ModelAndView modelAndView = new ModelAndView("List");
        Page<Product> products = s.isPresent() ? search(s, pageInfo) : getPage(pageInfo);
        modelAndView.addObject("keyword", s.orElse(null));
        modelAndView.addObject("product", products);
        return modelAndView;
    }


    //    @GetMapping
//    public ModelAndView showList(Pageable pageInfo) {
//        ModelAndView modelAndView = new ModelAndView("List");
//        Page<Product> products = productService.findAll(pageInfo);
//        modelAndView.addObject("product", products);
//        return modelAndView;
//    }



    @GetMapping("/create")
    public ModelAndView showCreateForm() {
        ModelAndView modelAndView = new ModelAndView("create");
        modelAndView.addObject("product", new Product());
        return modelAndView;
    }

    @PostMapping("/create")
    public ModelAndView saveProduct(@ModelAttribute("product") Product product) {
        ModelAndView modelAndView = new ModelAndView("create");
        productService.save(product);
        modelAndView.addObject("product", new Product());
        return modelAndView;
    }

    @GetMapping("/edit/{id}")
    public ModelAndView updateProduct(@PathVariable Long id) {
        Optional<Product> product = productService.findById(id);
        ModelAndView modelAndView = new ModelAndView("edit");
        modelAndView.addObject("product", product);
        return modelAndView;

    }

    @PostMapping("/edit")
    public ModelAndView updateProduct(@ModelAttribute("product") Product product, Pageable pageInfo) {
        ModelAndView modelAndView = new ModelAndView("List");
        productService.save(product);
        Page<Product> products = productService.findAll(pageInfo);
        modelAndView.addObject("product", products);
        return modelAndView;
    }

    @GetMapping("/delete/{id}")
    public ModelAndView showDeleteForm(@PathVariable Long id, Pageable pageable) {
        productService.delete(id);
        Page<Product> products = productService.findAll(pageable);
        ModelAndView modelAndView = new ModelAndView("List");
        modelAndView.addObject("product", products);
        return modelAndView;
    }
}
