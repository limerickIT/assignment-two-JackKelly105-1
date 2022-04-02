/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;

import Model.Brewery;
import Repository.BreweryRepo;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Jack Kelly
 */
@Service
public class BreweryService {

    @Autowired
    private BreweryRepo breweryRepo;

    public Brewery findOne(Long id) {
        return breweryRepo.findById(id).get();
    }

    public List<Brewery> findAll() {
        return (List<Brewery>) breweryRepo.findAll();
    }

    public long count() {
        return breweryRepo.count();
    }

    public void deleteByID(long breweryID) {
        breweryRepo.deleteById(breweryID);
    }

    public void saveBrewery(Brewery a) {
        breweryRepo.save(a);
    }

}
