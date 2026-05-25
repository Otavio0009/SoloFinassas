package com.example.solofinassas.controller;

import com.example.solofinassas.exception.TransacaoException;
import com.example.solofinassas.model.Despesa;
import com.example.solofinassas.model.Receita;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import java.time.LocalDate;

public class DashboardController {

    @FXML private VBox rootContainer;
    @FXML private Label lblTotalPoupanca;
    @FXML private Label lblTotalReceita;
    @FXML private Label lblTotalDespesa;
    @FXML private PieChart financeChart;

    @FXML private TextField txtDescription;
    @FXML private TextField txtAmount;
    @FXML private ComboBox<String> cbType;
    @FXML private TextField txtTag;

    @FXML private TableView<Receita> tabelaReceitas;
    @FXML private TableColumn<Receita, String> colRecDesc;
    @FXML private TableColumn<Receita, Double> colRecValor;
    @FXML private TableColumn<Receita, String> colRecTag;

    @FXML private TableView<Despesa> tabelaDespesas;
    @FXML private TableColumn<Despesa, String> colDesDesc;
    @FXML private TableColumn<Despesa, Double> colDesValor;
    @FXML private TableColumn<Despesa, String> colDesTag;

    private ObservableList<Receita> listaReceitas = FXCollections.observableArrayList();
    private ObservableList<Despesa> listaDespesas = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        if (rootContainer != null && !rootContainer.getStyleClass().contains("light-theme") && !rootContainer.getStyleClass().contains("dark-theme")) {
            rootContainer.getStyleClass().add("light-theme");
        }

        if (cbType != null) {
            cbType.setItems(FXCollections.observableArrayList("Selecione...", "Receita", "Despesa"));
            cbType.getSelectionModel().select(0);
        }
        configurarColunas();
        atualizarTotais();
    }

    private void configurarColunas() {
        colRecDesc.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        colRecValor.setCellValueFactory(new PropertyValueFactory<>("valor"));
        colRecTag.setCellValueFactory(new PropertyValueFactory<>("tag"));

        colDesDesc.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        colDesValor.setCellValueFactory(new PropertyValueFactory<>("valor"));
        colDesTag.setCellValueFactory(new PropertyValueFactory<>("tag"));

        tabelaReceitas.setItems(listaReceitas);
        tabelaDespesas.setItems(listaDespesas);
    }

    @FXML
    public void handleAdicionarMovimentacao(ActionEvent event) {
        try {
            validarCampos();

            String desc = txtDescription.getText();
            double valor = Double.parseDouble(txtAmount.getText());
            String tipo = cbType.getValue();
            String tag = txtTag.getText();

            if (valor <= 0) {
                throw new TransacaoException("O valor deve ser maior que zero.");
            }

            if ("Receita".equals(tipo)) {
                Receita novaReceita = new Receita(listaReceitas.size() + 1, desc, valor, LocalDate.now(), tag, "Geral", "Aporte");
                listaReceitas.add(novaReceita);
            } else {
                Despesa novaDespesa = new Despesa(listaDespesas.size() + 1, desc, valor, LocalDate.now(), tag, "Geral", "Geral");
                listaDespesas.add(novaDespesa);
            }

            limparCampos();
            atualizarTotais();

        } catch (NumberFormatException e) {
            exibirAlerta("Erro de Formato", "Insira um número decimal válido usando ponto (.)");
        } catch (TransacaoException e) {
            exibirAlerta("Validação", e.getMessage());
        }
    }

    private void validarCampos() throws TransacaoException {
        if (txtDescription.getText().isEmpty() || txtAmount.getText().isEmpty() ||
                cbType.getValue() == null || "Selecione...".equals(cbType.getValue()) || txtTag.getText().isEmpty()) {
            throw new TransacaoException("Todos os campos precisam estar preenchidos.");
        }
    }

    private void atualizarTotais() {
        double totalRec = listaReceitas.stream().mapToDouble(Receita::getValor).sum();
        double totalDes = listaDespesas.stream().mapToDouble(Despesa::getValor).sum();
        double poupanca = totalRec - totalDes;

        lblTotalReceita.setText(String.format("R$ %.2f", totalRec));
        lblTotalDespesa.setText(String.format("R$ %.2f", totalDes));
        lblTotalPoupanca.setText(String.format("R$ %.2f", poupanca));

        atualizarGraficoDoughnut(totalRec, totalDes);
    }

    private void atualizarGraficoDoughnut(double rec, double des) {
        double totalGeral = rec + des;

        String labelReceita = "Receitas";
        String labelDespesa = "Despesas";

        if (totalGeral > 0) {
            double pctRec = (rec / totalGeral) * 100;
            double pctDes = (des / totalGeral) * 100;

            labelReceita = String.format("Receitas (%.1f%%)", pctRec);
            labelDespesa = String.format("Despesas (%.1f%%)", pctDes);
        } else {
            labelReceita = "Receitas (0.0%)";
            labelDespesa = "Despesas (0.0%)";
        }

        ObservableList<PieChart.Data> dados = FXCollections.observableArrayList(
                new PieChart.Data(labelReceita, rec),
                new PieChart.Data(labelDespesa, des)
        );

        financeChart.setData(dados);
    }

    private void limparCampos() {
        txtDescription.clear();
        txtAmount.clear();
        txtTag.clear();

        if (cbType != null) {
            cbType.getSelectionModel().select(0);
        }
    }

    private void exibirAlerta(String tit, String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(tit);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    @FXML
    public void handleAlternarTema(ActionEvent event) {
        if (rootContainer != null) {
            if (rootContainer.getStyleClass().contains("light-theme")) {
                rootContainer.getStyleClass().remove("light-theme");
                rootContainer.getStyleClass().add("dark-theme");
            } else {
                rootContainer.getStyleClass().remove("dark-theme");
                rootContainer.getStyleClass().add("light-theme");
            }
        }
    }
}