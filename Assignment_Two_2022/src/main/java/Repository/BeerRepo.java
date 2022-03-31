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
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BeerRepo extends CrudRepository<Beer, Long> {

    @Query("SELECT b FROM Beer b WHERE b.id = :searchterm")
    public List<Beer> getBeers(int searchterm);
}
