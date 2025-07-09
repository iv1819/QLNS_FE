/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Presenter;

import com.fasterxml.jackson.annotation.JsonCreator.Mode;

import API.PublisherApiClient;

/**
 *
 * @author nam11
 */
public class PublisherP {
    private Model.Publisher publisherModel;
    private View.Publisher publisherView;
    private PublisherApiClient publisherApiClient;
    public PublisherP(View.Publisher publisherView) {
        this.publisherView = publisherView;
        this.publisherModel = new Model.Publisher();
        this.publisherApiClient = new PublisherApiClient();
    }
    public void loadAllPublishers() {
        try {
            var publishers = publisherApiClient.getAllPublishers();
            publisherView.displayPublishers(publishers);
        } catch (Exception e) {
            publisherView.showError("Failed to load publishers: " + e.getMessage());
        }
    }
    
    public void addPublisher(String tenNXB, String sdt) {
        // TODO Auto-generated method stub
        try {
            Model.Publisher newPublisher = new Model.Publisher();
            newPublisher.setTenNXB(tenNXB);
            newPublisher.setSdt(sdt);
            publisherApiClient.addPublisher(newPublisher);
            publisherView.showSuccess("Publisher added successfully.");
            loadAllPublishers();
        } catch (Exception e) {
            publisherView.showError("Failed to add publisher: " + e.getMessage());
        }
    }
    public void updatePublisher(String maNXB, String tenNXB, String sdt) {
        // TODO Auto-generated method stub
        try {
            Model.Publisher updatedPublisher = new Model.Publisher();
            updatedPublisher.setMaNXB(maNXB);
            updatedPublisher.setTenNXB(tenNXB);
            updatedPublisher.setSdt(sdt);
            publisherApiClient.updatePublisher(maNXB, updatedPublisher);
            publisherView.showSuccess("Publisher updated successfully.");
            loadAllPublishers();
        } catch (Exception e) {
            publisherView.showError("Failed to update publisher: " + e.getMessage());
        }
    }
    public void deletePublisher(String maNXB) {
        try {
            publisherApiClient.deletePublisher(maNXB);
            publisherView.showSuccess("Publisher deleted successfully.");
            loadAllPublishers();
        } catch (Exception e) {
            publisherView.showError("Failed to delete publisher: " + e.getMessage());
        }
    }
    
    
}
