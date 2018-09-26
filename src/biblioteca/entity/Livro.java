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
package biblioteca.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "livros")
public class Livro  implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    
    @Column(nullable = false)
    private String titulo;
    
    @Column
    private String capa;
    
    @Column(nullable = false)
    private String autor;
    
    @Column(nullable = false)
    private short lancamento;
    
    @Column(nullable = false)
    private float nota;
    
    @Column
    private String resenha;
    
    @Column(nullable = false)
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date cadastro;

    public Livro() {
    }
    public Livro(Integer id, String titulo, String autor, short lancamento, float nota, Date cadastro) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.lancamento = lancamento;
        this.nota = nota;
        this.cadastro = cadastro;
    }
    public Livro(Integer id, String titulo, String capa, String autor, short lancamento, float nota, String resenha, Date cadastro) {
       this.id = id;
       this.titulo = titulo;
       this.capa = capa;
       this.autor = autor;
       this.lancamento = lancamento;
       this.nota = nota;
       this.resenha = resenha;
       this.cadastro = cadastro;
    }
   
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    public String getTitulo() {
        return this.titulo;
    }
    
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    public String getCapa() {
        return this.capa;
    }
    
    public void setCapa(String capa) {
        this.capa = capa;
    }
    public String getAutor() {
        return this.autor;
    }
    
    public void setAutor(String autor) {
        this.autor = autor;
    }
    public short getLancamento() {
        return this.lancamento;
    }
    
    public void setLancamento(short lancamento) {
        this.lancamento = lancamento;
    }
    public float getNota() {
        return this.nota;
    }
    
    public void setNota(float nota) {
        this.nota = nota;
    }
    public String getResenha() {
        return this.resenha;
    }
    
    public void setResenha(String resenha) {
        this.resenha = resenha;
    }
    public Date getCadastro() {
        return this.cadastro;
    }
    
    public void setCadastro(Date cadastro) {
        this.cadastro = cadastro;
    }

    @Override
    public String toString() {
        return "ID: " + id + ", Titulo: " + titulo + ", Autor: " + autor + ", Ano: " + lancamento;
    }

}