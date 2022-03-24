/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Exception;

public class BeerNotFound extends RuntimeException{
    public  BeerNotFound(Long id) {
    super("Could not find beer " + id);
  }
}
