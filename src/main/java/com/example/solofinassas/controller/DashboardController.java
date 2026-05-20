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

import java.time.LocalDate;

public class DashboardController {

    @FXML private Label lblTotalReceita;
    @FXML private Label lblTotalDespesa;
    @FXML private PieChart financeChart;

    @FXML private TextField txtDescricao;
    @FXML private TextField txtValor;
    @FXML private ComboBox<String> cbTipo;
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
        // Agora o cbTipo está protegido dentro do método correto!
        if (cbTipo != null) {
            cbTipo.setItems(FXCollections.observableArrayList("Entrada", "Saída"));
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

            String desc = txtDescricao.getText();
            double valor = Double.parseDouble(txtValor.getText());
            String tipo = cbTipo.getValue();
            String tag = txtTag.getText();

            if (valor <= 0) {
                throw new TransacaoException("O valor deve ser maior que zero.");
            }

            if ("Entrada".equals(tipo)) {
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
        if (txtDescricao.getText().isEmpty() || txtValor.getText().isEmpty() || cbTipo.getValue() == null || txtTag.getText().isEmpty()) {
            throw new TransacaoException("Todos os campos precisam estar preenchidos.");
        }
    }

    private void atualizarTotais() {
        double totalRec = listaReceitas.stream().mapToDouble(Receita::getValor).sum();
        double totalDes = listaDespesas.stream().mapToDouble(Despesa::getValor).sum();

        lblTotalReceita.setText(String.format("R$ %.2f", totalRec));
        lblTotalDespesa.setText(String.format("R$ %.2f", totalDes));

        atualizarGraficoDoughnut(totalRec, totalDes);
    }

    private void atualizarGraficoDoughnut(double rec, double des) {
        ObservableList<PieChart.Data> dados = FXCollections.observableArrayList(
                new PieChart.Data("Receitas", rec),
                new PieChart.Data("Despesas", des)
        );
        financeChart.setData(dados);
    }

    private void limparCampos() {
        txtDescricao.clear();
        txtValor.clear();
        txtTag.clear();
        cbTipo.setValue(null);
    }

    private void exibirAlerta(String tit, String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(tit);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}