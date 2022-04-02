/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Repository;
import Model.Brewery;
import org.springframework.data.repository.CrudRepository;
/**
 *
 * @author Jack Kelly
 */
public interface BreweryRepo  extends CrudRepository<Brewery, Long>{
    
}
