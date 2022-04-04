/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;

import Model.Category;
import Repository.CategoryRepo;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Jack Kelly
 */
@Service
public class CategoryService {
    
    @Autowired
    private CategoryRepo categoryRepo;

    public Category findOne(Long id) {
        return categoryRepo.findById(id).get();
    }

    public List<Category> findAll() {
        return (List<Category>) categoryRepo.findAll();
    }

    public long count() {
        return categoryRepo.count();
    }

    public void deleteByID(long beerID) {
        categoryRepo.deleteById(beerID);
    }

    public void saveBeer(Category a) {
        categoryRepo.save(a);
    }  
}
