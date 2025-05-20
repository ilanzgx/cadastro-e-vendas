package main.java.ui.views.venda;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

import main.java.controllers.VendaController;
import main.java.entities.Venda;
import main.java.ui.ScreenManager;
import main.java.utils.CpfUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.Font;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

public class VendaListDetalhesView extends JPanel {
    private ScreenManager screenManager;
    private VendaController vendaController;

    private Venda vendaAtual;
    private JLabel labelId, labelData, labelCliente, labelTotal;
    private DefaultTableModel defaultTableModel;

    public VendaListDetalhesView(ScreenManager screenManager) {
        this.screenManager = screenManager;
        this.vendaController = new VendaController();
        setLayout(new BorderLayout(10, 10));

        JPanel infoPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        infoPanel.add(new JLabel("ID da Venda:"));
        labelId = new JLabel();
        infoPanel.add(labelId);

        infoPanel.add(new JLabel("Data:"));
        labelData = new JLabel();
        infoPanel.add(labelData);

        infoPanel.add(new JLabel("Cliente:"));
        labelCliente = new JLabel();
        infoPanel.add(labelCliente);

        infoPanel.add(new JLabel("Total:"));
        labelTotal = new JLabel();
        labelTotal.setFont(new Font("SansSerif", Font.BOLD, 14));
        infoPanel.add(labelTotal);

        // Tabela de produtos
        String[] colunas = {"Produto", "Preço Unitário", "Quantidade", "Subtotal"};
        defaultTableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable produtosTable = new JTable(defaultTableModel);
        produtosTable.setAutoCreateRowSorter(true);

        // Botão de voltar
        JButton buttonVoltar = new JButton("Voltar");
        buttonVoltar.addActionListener(event -> voltarParaLista());

        JButton buttonGerarPdf = new JButton("Gerar PDF");
        buttonGerarPdf.addActionListener(event -> gerarVendaPdf());

        // Painel de botões
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(buttonVoltar);
        buttonPanel.add(buttonGerarPdf);

        // Layout principal
        add(infoPanel, BorderLayout.NORTH);
        add(new JScrollPane(produtosTable), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public void carregarVenda(int vendaId) {
        vendaAtual = vendaController.listarVendaId(vendaId);
        if (vendaAtual != null) {
            atualizarInformacoes();
            atualizarTabelaProdutos();
        } else {
            JOptionPane.showMessageDialog(this, "Venda não encontrada!", "Erro", JOptionPane.ERROR_MESSAGE);
            voltarParaLista();
        }
    }

    private void atualizarInformacoes() {
        labelId.setText(String.valueOf(vendaAtual.getId()));
        labelData.setText(vendaAtual.getData());
        labelCliente.setText(String.format("%s (CPF: %s)", vendaAtual.getCliente().getNome(), CpfUtils.format(vendaAtual.getCliente().getCpf())));

        double total = vendaAtual.getProdutosQuantidades().entrySet().stream()
                .mapToDouble(e -> e.getKey().getPreco() * e.getValue())
                .sum();
        labelTotal.setText(String.format("R$ %.2f", total));
    }

    private void atualizarTabelaProdutos() {
        defaultTableModel.setRowCount(0);

        vendaAtual.getProdutosQuantidades().forEach((produto, quantidade) -> {
            double subtotal = produto.getPreco() * quantidade;
            defaultTableModel.addRow(new Object[]{
                    produto.getNome(),
                    String.format("R$ %.2f", produto.getPreco()),
                    quantidade,
                    String.format("R$ %.2f", subtotal)
            });
        });
    }

    private void gerarVendaPdf() {
        if(vendaAtual == null) {
            JOptionPane.showMessageDialog(this, "Nenhuma venda carregada.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(String.format("venda_id%d.pdf", vendaAtual.getId())));
            document.open();

            com.itextpdf.text.Font fontTitulo = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Paragraph paragraphTitulo = new Paragraph("Detalhes da Venda", fontTitulo);
            paragraphTitulo.setAlignment(Element.ALIGN_CENTER);
            document.add(paragraphTitulo);

            document.add(new Paragraph(" "));
            LineSeparator ls = new LineSeparator();
            document.add(new Chunk(ls));
            document.add(new Paragraph(" "));

            document.add(new Paragraph(String.format("ID da Venda: %d", vendaAtual.getId())));

            LocalDateTime dataHora = LocalDateTime.parse(vendaAtual.getData());
            String dataFormatada = dataHora.toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            document.add(new Paragraph(String.format("Data: %s", dataFormatada)));

            document.add(new Paragraph(String.format("Cliente: %s", vendaAtual.getCliente().getNome())));
            document.add(new Paragraph(String.format("CPF: %s", CpfUtils.format(vendaAtual.getCliente().getCpf()))));

            document.add(new Paragraph(" "));

            PdfPTable pdfPTable = new PdfPTable(4);
            pdfPTable.setWidthPercentage(100);
            pdfPTable.setSpacingBefore(10f);
            pdfPTable.setSpacingAfter(10f);

            com.itextpdf.text.Font headerFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 12, Font.BOLD);
            BaseColor headerColor = new BaseColor(230, 230, 230);

            Stream.of("Produto", "Preço Unitário", "Quantidade", "Subtotal").forEach(col -> {
                PdfPCell cell = new PdfPCell(new Phrase(col, headerFont));
                cell.setBackgroundColor(headerColor);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                pdfPTable.addCell(cell);
            });

            vendaAtual.getProdutosQuantidades().forEach((produto, quantidade) -> {
                Double subtotal = produto.getPreco() * quantidade;

                PdfPCell produtoCell = new PdfPCell(new Phrase(produto.getNome()));
                produtoCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                pdfPTable.addCell(produtoCell);

                PdfPCell precoCell = new PdfPCell(new Phrase(String.format("R$ %.2f", produto.getPreco())));
                precoCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                pdfPTable.addCell(precoCell);

                PdfPCell quantidadeCell = new PdfPCell(new Phrase(String.valueOf(quantidade)));
                quantidadeCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                pdfPTable.addCell(quantidadeCell);

                PdfPCell totalCell = new PdfPCell(new Phrase(String.format("R$ %.2f", subtotal)));
                totalCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                pdfPTable.addCell(totalCell);
            });

            document.add(pdfPTable);

            document.add(new Paragraph(" "));

            com.itextpdf.text.Font fontTotal = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 14, Font.BOLD);
            Paragraph paragraphTotal = new Paragraph(String.format("Total: %s", labelTotal.getText()), fontTotal);
            paragraphTotal.setAlignment(Element.ALIGN_RIGHT);
            document.add(paragraphTotal);

            DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("H:mm:ss");
            DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            com.itextpdf.text.Font fontRodape = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 9, Font.ITALIC);
            Paragraph paragraphRodape = new Paragraph(String.format("Gerado em %s as %s", java.time.LocalDate.now().format(formatterDate), java.time.LocalTime.now().format(formatterTime)), fontRodape);
            paragraphRodape.setAlignment(Element.ALIGN_CENTER);
            document.add(new Paragraph(" "));
            document.add(paragraphRodape);

            document.close();
            JOptionPane.showMessageDialog(this, "PDF gerado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception error) {
            JOptionPane.showMessageDialog(this, "Erro ao gerar PDF: " + error.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            error.printStackTrace();
        }
    }

    private void voltarParaLista() {
        screenManager.showVendaListView();
    }
}
