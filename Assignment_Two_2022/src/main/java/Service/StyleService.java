/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;

import Model.Style;
import Repository.StyleRepo;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Jack Kelly
 */
@Service
public class StyleService {
    
    @Autowired
    private StyleRepo styleRepo;

    public Style findOne(Long id) {
        return styleRepo.findById(id).get();
    }

    public List<Style> findAll() {
        return (List<Style>) styleRepo.findAll();
    }

    public long count() {
        return styleRepo.count();
    }

    public void deleteByID(long beerID) {
        styleRepo.deleteById(beerID);
    }

    public void saveBeer(Style a) {
        styleRepo.save(a);
    }  
}
