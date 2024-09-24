package com.example.c195mobileapp.controllers;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.c195mobileapp.R;

public class AddCourseController extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.addcoursesactivity);
    }
}

//@FXML
//    void OnActionAddPartToProduct(ActionEvent event) {
//        Part selectedPart = PartTableView.getSelectionModel().getSelectedItem();
//        if (selectedPart != null) {
//            newProduct.addAssociatedPart(selectedPart);
//            AssociatedPartTableView.setItems(newProduct.getAllAssociatedParts());
//        }
//    }

//void OnActionRemovePartFromProduct(ActionEvent event) {
//    Part selectedPart = PartTableView.getSelectionModel().getSelectedItem();
//    if (selectedPart != null) {
//        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
//        alert.setTitle("Delete Product?");
//        alert.setHeaderText("Are you sure you want to delete the selected product?");
//        Optional<ButtonType> result = alert.showAndWait();
//        if (result.get() == ButtonType.OK) {
//            newProduct.deleteAssociatedPart(selectedPart);
//            AssociatedPartTableView.setItems(newProduct.getAllAssociatedParts());
//        } else if (result.get() == ButtonType.CANCEL) {
//            return;
//        }
//    }
//}