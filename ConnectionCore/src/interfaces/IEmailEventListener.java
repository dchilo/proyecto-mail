/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import java.util.List;
import utils.Email;

/**
 *
 * @author Ronaldo Rivero
 */
public interface IEmailEventListener {
    void onReceiveEmailEvent(List<Email> emails);
}
