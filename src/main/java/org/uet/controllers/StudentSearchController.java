package org.uet.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.fxml.FXML;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;

public class StudentSearchController {
    @FXML
    private TableView<Student> studentTable;

    @FXML
    private TableColumn<Student, Integer> colSTT;

    @FXML
    private TableColumn<Student, String> colStudentId;

    @FXML
    private TableColumn<Student, String> colName;

    @FXML
    private TableColumn<Student, String> colGender;

    @FXML
    private TableColumn<Student, String> colClass;

    @FXML
    private TableColumn<Student, String> colMajor;

    @FXML
    private TableColumn<Student, String> colPhone;

    @FXML
    private TableColumn<Student, String> colEmail;

    @FXML
    private TableColumn<Student, String> colPassword;

    @FXML
    private TextField txtStudentId, txtName, txtGender, txtStudentClass, txtMajor,
            txtPhone, txtEmail, txtPassword;

    private ObservableList<Student> studentList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colSTT.setCellValueFactory(new PropertyValueFactory<>("STT"));
        colStudentId.setCellValueFactory(new PropertyValueFactory<>("Mã SV"));
        colName.setCellValueFactory(new PropertyValueFactory<>("Họ và tên"));
        colGender.setCellValueFactory(new PropertyValueFactory<>("Giới tính"));
        colClass.setCellValueFactory(new PropertyValueFactory<>("Lớp khóa học"));
        colMajor.setCellValueFactory(new PropertyValueFactory<>("Chuyên ngành"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("Số ĐT"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("Email"));
        colPassword.setCellValueFactory(new PropertyValueFactory<>("Mật khẩu"));

        studentTable.setItems(studentList);
        loadStudentList();
    }

    private void loadStudentList() {
        try (InputStream is = getClass().getResourceAsStream("/data/student.csv");
             BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            String line;
            studentList.clear();
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length == 8 && !values[0].equals("STT")) {
                    studentList.add(new Student(values[0], values[1], values[2],
                            values[3], values[4], values[5], values[6], values[7]));
                }
            }
            System.out.println("Đang tải danh sách sinh viên ...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void sortList() {
        ObservableList<Student> filteredList = FXCollections.observableArrayList();

        String studentId = txtStudentId.getText().toLowerCase();
        String name = txtName.getText().toLowerCase();
        String gender = txtGender.getText().toLowerCase();
        String studentClass = txtStudentClass.getText().toLowerCase();
        String major = txtMajor.getText().toLowerCase();
        String phone = txtPhone.getText().toLowerCase();
        String email = txtEmail.getText().toLowerCase();
        String password = txtPassword.getText().toLowerCase();

        int stt = 1;
        for (Student student : studentList) {
            if ((studentId.isEmpty() || student.getStudentId().toLowerCase().contains(studentId))
                    && (name.isEmpty() || student.getName().toLowerCase().contains(name))
                    && (gender.isEmpty() || student.getGender().toLowerCase().contains(gender))
                    && (studentClass.isEmpty() || student.getStudentClass().toLowerCase().contains(studentClass))
                    && (major.isEmpty() || student.getMajor().toLowerCase().contains(major))
                    && (phone.isEmpty() || student.getPhone().toLowerCase().contains(phone))
                    && (email.isEmpty() || student.getEmail().toLowerCase().contains(email))
                    && (password.isEmpty() || student.getPassword().toLowerCase().contains(password))) {
                student.setStt(stt++);
                filteredList.add(student);
            }
        }
        studentTable.setItems(filteredList);
    }
}
