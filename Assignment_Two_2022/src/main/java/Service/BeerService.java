/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;

/**
 *
 * @author Jack Kelly
 */
import Model.Beer;
import Repository.BeerRepo;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BeerService {
    
     @Autowired
    private BeerRepo beerRepo;

    public Beer findOne(Long id) {
        return beerRepo.findById(id).get();
    }

    public List<Beer> findAll() {
        return (List<Beer>) beerRepo.findAll();
    }

    public long count() {
        return beerRepo.count();
    }

    public void deleteByID(long beerID) {
        beerRepo.deleteById(beerID);
    }

    public void saveBeer(Beer a) {
        beerRepo.save(a);
    }  

   
    
}
