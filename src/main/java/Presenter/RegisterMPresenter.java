/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Presenter;

import API.AccountApiClient;
import View.interfaces.IRegisterM;
import java.util.List;

/**
 *
 * @author trang
 */
public class RegisterMPresenter {

    private IRegisterM view;
    private AccountApiClient accountApiClient;

    public RegisterMPresenter(IRegisterM view){
        this.view = view;
        this.accountApiClient = new AccountApiClient();
    }




}