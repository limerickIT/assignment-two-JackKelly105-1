/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Repository;

/**
 *
 * @author Jack Kelly
 */
import Model.Beer;
import org.springframework.data.repository.CrudRepository;
public interface BeerRepo extends CrudRepository<Beer, Long> {
    
}


