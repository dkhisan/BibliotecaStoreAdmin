/*
 * Copyright (C) 2018 hisan
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package biblioteca.gui;

import biblioteca.dao.LivroDAO;
import biblioteca.dao.exceptions.LivroNaoEncontradoException;
import biblioteca.entity.Livro;
import biblioteca.gui.exceptions.CampoObrigatorioVazioException;
import biblioteca.gui.exceptions.NotaForaDoIntervaloException;
import biblioteca.util.BibliotecaStoreUtil;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.hibernate.SessionFactory;

public class InformacaoLivroForm extends javax.swing.JFrame {
    private JFileChooser imagemChooser = null;
    private File imagemSelected = null;
    private Livro livro = null;

    public InformacaoLivroForm(Livro livro) {
        imagemChooser = new JFileChooser();
        imagemChooser.setDialogTitle("Escolher imagem");
        imagemChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        imagemChooser.setFileFilter(new FileNameExtensionFilter(
                "Imagem", "jpg", "png"));
        initComponents();
        reOpen(livro);
    }
    
    public void reOpen(Livro livro) {
        this.livro = livro;
        preencherCampos();
        setLocationRelativeTo((java.awt.Component) SwingUtilities.getWindowAncestor(this));
        setVisible(true);
    }
    
    private void atualizarLivro() throws CampoObrigatorioVazioException, NotaForaDoIntervaloException {
        SessionFactory factory = null;
        LivroDAO dao = null;
        Livro livroEdt = null;
        
        int    ano = 0;
        float nota = 0.0f;
        
        if (txTitulo.getText().trim() == null || txTitulo.getText().trim().equals("") ||
            txAutor.getText().trim() == null || txAutor.getText().trim().equals("") ||
            txAnoLancamento.getText().trim() == null || txAnoLancamento.getText().trim().equals("") ||
            txNotaLeitor.getText().trim() == null || txNotaLeitor.getText().trim().equals(""))
            throw new CampoObrigatorioVazioException("Alguns campos estão vazios, verifique-os e tente novamente.");
        
        try {
            // Teste se o usuário colocou ',' como precisão decimal
            if (txNotaLeitor.getText().trim().contains(",")) {
                txNotaLeitor.setText(txNotaLeitor.getText().trim().replace(',', '.'));
            }
            ano  = Integer.parseInt(txAnoLancamento.getText().trim());
            nota = Float.parseFloat(txNotaLeitor.getText().trim());
        } catch (NumberFormatException e) {
            throw(e);
        }
        
        if (nota < 0 || nota > 10) {
            throw new NotaForaDoIntervaloException("A nota informada está fora do intervalo de 0 à 10.");
        }
        
        factory = BibliotecaStoreUtil.getSessionFactory();
        dao = new LivroDAO(factory);
        livroEdt = new Livro();
        
        livroEdt.setTitulo(txTitulo.getText().trim());
        livroEdt.setAutor(txAutor.getText().trim());
        livroEdt.setLancamento((short)ano);
        livroEdt.setNota(nota);
        
        if (txResenha.getText().trim() != null || txResenha.getText().trim().equals("")) {
            livroEdt.setResenha(txResenha.getText().trim());
        }
        
        try {
            dao.edit(livro.getId(), livroEdt);
        } catch (LivroNaoEncontradoException e) {
            JOptionPane.showMessageDialog(this, e.getLocalizedMessage(), "Atenção", JOptionPane.WARNING_MESSAGE);
        }
        
        if (dao.isLastSuccess()) {
            JOptionPane.showMessageDialog(this, String.format(
                    "O Livro id \"%d\" foi atualizado com êxito.", livro.getId()));
            
            btnProcurarImagem.setEnabled(false);
            btnEditar.setEnabled(true);
            btnSalvar.setEnabled(false);
            btnApagar.setEnabled(false);
            txTitulo.setEnabled(false);
            txAutor.setEnabled(false);
            txAnoLancamento.setEnabled(false);
            txNotaLeitor.setEnabled(false);
            txResenha.setEnabled(false);
        } else {
            JOptionPane.showMessageDialog(this,
                    dao.getLastErrorMessage(), "Falha", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void apagarLivro() {
        SessionFactory factory = null;
        LivroDAO dao = null;
        
        factory = BibliotecaStoreUtil.getSessionFactory();
        dao = new LivroDAO(factory);
        
        try {
            dao.destroy(livro.getId());
        } catch (LivroNaoEncontradoException e) {
            JOptionPane.showMessageDialog(this, e.getLocalizedMessage(), "Atenção", JOptionPane.WARNING_MESSAGE);
        }
        
        if (dao.isLastSuccess()) {
            JOptionPane.showMessageDialog(this, String.format(
                    "O Livro id \"%d\" foi removido dos registros.", livro.getId()));
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                    dao.getLastErrorMessage(), "Falha", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void preencherCampos() {
        lbLivroId.setText(new StringBuilder("Código: ")
                .append(String.valueOf(livro.getId())).toString());
        txTitulo.setText(livro.getTitulo());
        txAutor.setText(livro.getAutor());
        txAnoLancamento.setText(String.valueOf(livro.getLancamento()));
        txNotaLeitor.setText(String.valueOf(livro.getNota()));
        txResenha.setText(livro.getResenha());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        lbLivroId = new javax.swing.JLabel();
        pnCentroInfo = new javax.swing.JPanel();
        lbTitulo = new javax.swing.JLabel();
        txTitulo = new javax.swing.JTextField();
        lbImagem = new javax.swing.JLabel();
        pnChooseImagem = new javax.swing.JPanel();
        btnProcurarImagem = new javax.swing.JButton();
        lbTxImagem = new javax.swing.JLabel();
        lbAutor = new javax.swing.JLabel();
        txAutor = new javax.swing.JTextField();
        pnAnoNota = new javax.swing.JPanel();
        lbAno = new javax.swing.JLabel();
        txAnoLancamento = new javax.swing.JTextField();
        lbNota = new javax.swing.JLabel();
        txNotaLeitor = new javax.swing.JTextField();
        lbResenha = new javax.swing.JLabel();
        pnResenha = new javax.swing.JPanel();
        scrlResenha = new javax.swing.JScrollPane();
        txResenha = new javax.swing.JTextArea();
        pnEsq = new javax.swing.JPanel();
        pnDir = new javax.swing.JPanel();
        pnImagem = new javax.swing.JPanel();
        lbImagemView = new javax.swing.JLabel();
        pnImagemDir = new javax.swing.JPanel();
        pnSulBotoes = new javax.swing.JPanel();
        btnApagar = new javax.swing.JButton();
        btnSalvar = new javax.swing.JButton();
        btnEditar = new javax.swing.JButton();
        btnVoltar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Informações");
        setMaximumSize(new java.awt.Dimension(574, 345));
        setMinimumSize(new java.awt.Dimension(574, 345));
        setResizable(false);
        setSize(new java.awt.Dimension(574, 345));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });
        getContentPane().setLayout(new java.awt.BorderLayout(10, 10));

        lbLivroId.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        lbLivroId.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbLivroId.setText("%id%");
        getContentPane().add(lbLivroId, java.awt.BorderLayout.PAGE_START);

        java.awt.GridBagLayout jPanel4Layout = new java.awt.GridBagLayout();
        jPanel4Layout.columnWidths = new int[] {0, 5, 0, 5, 0};
        jPanel4Layout.rowHeights = new int[] {0, 5, 0, 5, 0, 5, 0, 5, 0};
        pnCentroInfo.setLayout(jPanel4Layout);

        lbTitulo.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbTitulo.setText("Título");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        pnCentroInfo.add(lbTitulo, gridBagConstraints);

        txTitulo.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.1;
        pnCentroInfo.add(txTitulo, gridBagConstraints);

        lbImagem.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbImagem.setText("Imagem");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        pnCentroInfo.add(lbImagem, gridBagConstraints);

        pnChooseImagem.setLayout(new java.awt.BorderLayout(5, 0));

        btnProcurarImagem.setText("Procurar");
        btnProcurarImagem.setEnabled(false);
        btnProcurarImagem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProcurarImagemActionPerformed(evt);
            }
        });
        pnChooseImagem.add(btnProcurarImagem, java.awt.BorderLayout.LINE_START);

        lbTxImagem.setText("Nenhuma imagem selecionada");
        pnChooseImagem.add(lbTxImagem, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        pnCentroInfo.add(pnChooseImagem, gridBagConstraints);

        lbAutor.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbAutor.setText("Autor");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        pnCentroInfo.add(lbAutor, gridBagConstraints);

        txAutor.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.1;
        pnCentroInfo.add(txAutor, gridBagConstraints);

        pnAnoNota.setLayout(new java.awt.GridLayout(1, 0));

        lbAno.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbAno.setText("Ano");
        pnAnoNota.add(lbAno);

        txAnoLancamento.setEnabled(false);
        pnAnoNota.add(txAnoLancamento);

        lbNota.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbNota.setText("Nota");
        pnAnoNota.add(lbNota);

        txNotaLeitor.setEnabled(false);
        pnAnoNota.add(txNotaLeitor);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        pnCentroInfo.add(pnAnoNota, gridBagConstraints);

        lbResenha.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbResenha.setText("Resenha");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        pnCentroInfo.add(lbResenha, gridBagConstraints);

        pnResenha.setLayout(new java.awt.BorderLayout());

        txResenha.setColumns(20);
        txResenha.setRows(5);
        txResenha.setEnabled(false);
        scrlResenha.setViewportView(txResenha);

        pnResenha.add(scrlResenha, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 0.1;
        pnCentroInfo.add(pnResenha, gridBagConstraints);

        getContentPane().add(pnCentroInfo, java.awt.BorderLayout.CENTER);
        getContentPane().add(pnEsq, java.awt.BorderLayout.LINE_START);

        pnDir.setLayout(new java.awt.BorderLayout());

        lbImagemView.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbImagemView.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout pnImagemLayout = new javax.swing.GroupLayout(pnImagem);
        pnImagem.setLayout(pnImagemLayout);
        pnImagemLayout.setHorizontalGroup(
            pnImagemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 196, Short.MAX_VALUE)
            .addGroup(pnImagemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(lbImagemView, javax.swing.GroupLayout.DEFAULT_SIZE, 196, Short.MAX_VALUE))
        );
        pnImagemLayout.setVerticalGroup(
            pnImagemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 275, Short.MAX_VALUE)
            .addGroup(pnImagemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(lbImagemView, javax.swing.GroupLayout.DEFAULT_SIZE, 275, Short.MAX_VALUE))
        );

        pnDir.add(pnImagem, java.awt.BorderLayout.CENTER);
        pnDir.add(pnImagemDir, java.awt.BorderLayout.LINE_END);

        getContentPane().add(pnDir, java.awt.BorderLayout.LINE_END);

        pnSulBotoes.setPreferredSize(null);

        btnApagar.setText("Apagar");
        btnApagar.setEnabled(false);
        btnApagar.setMaximumSize(new java.awt.Dimension(72, 32));
        btnApagar.setMinimumSize(new java.awt.Dimension(72, 32));
        btnApagar.setPreferredSize(new java.awt.Dimension(72, 32));
        btnApagar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnApagarActionPerformed(evt);
            }
        });
        pnSulBotoes.add(btnApagar);

        btnSalvar.setText("Salvar");
        btnSalvar.setEnabled(false);
        btnSalvar.setMaximumSize(new java.awt.Dimension(72, 32));
        btnSalvar.setMinimumSize(new java.awt.Dimension(72, 32));
        btnSalvar.setPreferredSize(new java.awt.Dimension(72, 32));
        btnSalvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalvarActionPerformed(evt);
            }
        });
        pnSulBotoes.add(btnSalvar);

        btnEditar.setText("Editar");
        btnEditar.setMaximumSize(new java.awt.Dimension(72, 32));
        btnEditar.setMinimumSize(new java.awt.Dimension(72, 32));
        btnEditar.setPreferredSize(new java.awt.Dimension(72, 32));
        btnEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarActionPerformed(evt);
            }
        });
        pnSulBotoes.add(btnEditar);

        btnVoltar.setText("Voltar");
        btnVoltar.setMaximumSize(new java.awt.Dimension(72, 32));
        btnVoltar.setMinimumSize(new java.awt.Dimension(72, 32));
        btnVoltar.setPreferredSize(new java.awt.Dimension(72, 32));
        btnVoltar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVoltarActionPerformed(evt);
            }
        });
        pnSulBotoes.add(btnVoltar);

        getContentPane().add(pnSulBotoes, java.awt.BorderLayout.PAGE_END);

        setBounds(0, 0, 621, 401);
    }// </editor-fold>//GEN-END:initComponents

    private void btnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarActionPerformed
        btnProcurarImagem.setEnabled(true);
        btnEditar.setEnabled(false);
        btnSalvar.setEnabled(true);
        btnApagar.setEnabled(true);
        txTitulo.setEnabled(true);
        txAutor.setEnabled(true);
        txAnoLancamento.setEnabled(true);
        txNotaLeitor.setEnabled(true);
        txResenha.setEnabled(true);
    }//GEN-LAST:event_btnEditarActionPerformed

    private void btnSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalvarActionPerformed
        try {
            atualizarLivro();
        } catch (CampoObrigatorioVazioException | NotaForaDoIntervaloException e) {
            JOptionPane.showMessageDialog(this, e.getLocalizedMessage(), "Atenção", javax.swing.JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_btnSalvarActionPerformed

    private void btnApagarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnApagarActionPerformed
        int opc = -1;
        opc = JOptionPane.showConfirmDialog(this, "Tem certeza que quer apagar este registro?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (opc == 0) {
            apagarLivro();
        }
    }//GEN-LAST:event_btnApagarActionPerformed

    private void btnVoltarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVoltarActionPerformed
        dispose();
    }//GEN-LAST:event_btnVoltarActionPerformed

    private void btnProcurarImagemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProcurarImagemActionPerformed
        int retorno = imagemChooser.showOpenDialog(this);
        if (retorno == JFileChooser.APPROVE_OPTION) {
            imagemSelected = imagemChooser.getSelectedFile();
            lbTxImagem.setText(imagemSelected.getName());
            lbImagemView.setIcon(new javax.swing.ImageIcon(imagemSelected.getAbsolutePath()));
        }
    }//GEN-LAST:event_btnProcurarImagemActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        lbTxImagem.setText("Nenhuma imagem selecionada.");
        lbImagemView.setIcon(null);
        lbImagemView.repaint();
    }//GEN-LAST:event_formWindowClosed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnApagar;
    private javax.swing.JButton btnEditar;
    private javax.swing.JButton btnProcurarImagem;
    private javax.swing.JButton btnSalvar;
    private javax.swing.JButton btnVoltar;
    private javax.swing.JLabel lbAno;
    private javax.swing.JLabel lbAutor;
    private javax.swing.JLabel lbImagem;
    private javax.swing.JLabel lbImagemView;
    private javax.swing.JLabel lbLivroId;
    private javax.swing.JLabel lbNota;
    private javax.swing.JLabel lbResenha;
    private javax.swing.JLabel lbTitulo;
    private javax.swing.JLabel lbTxImagem;
    private javax.swing.JPanel pnAnoNota;
    private javax.swing.JPanel pnCentroInfo;
    private javax.swing.JPanel pnChooseImagem;
    private javax.swing.JPanel pnDir;
    private javax.swing.JPanel pnEsq;
    private javax.swing.JPanel pnImagem;
    private javax.swing.JPanel pnImagemDir;
    private javax.swing.JPanel pnResenha;
    private javax.swing.JPanel pnSulBotoes;
    private javax.swing.JScrollPane scrlResenha;
    private javax.swing.JTextField txAnoLancamento;
    private javax.swing.JTextField txAutor;
    private javax.swing.JTextField txNotaLeitor;
    private javax.swing.JTextArea txResenha;
    private javax.swing.JTextField txTitulo;
    // End of variables declaration//GEN-END:variables

}
