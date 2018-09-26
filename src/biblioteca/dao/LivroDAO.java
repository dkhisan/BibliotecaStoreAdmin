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
package biblioteca.dao;

import biblioteca.dao.exceptions.LivroNaoEncontradoException;
import biblioteca.entity.Livro;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

public class LivroDAO {
    private SessionFactory factory = null;
    private boolean lastCommitSuccess;
    private String lastErrorMessage;
    
    /**
     * Instância o DAO com uma <code>Session</code> como parâmetro.
     * @param sessionFactory Instância de <code>Session</code>
     */
    public LivroDAO(SessionFactory sessionFactory) {
        this.factory = sessionFactory;
        this.lastCommitSuccess = false;
        this.lastErrorMessage = null;
    }
    
    /**
     * Adiciona um novo livro ao banco de dados.
     * @param livro <code>Livro</code> para adicionar ao banco
     */
    public void insert(Livro livro) {
        Session session = factory.openSession();
        Transaction transaction = null;
        lastCommitSuccess = false;
        try {
            transaction = session.getTransaction();
            transaction.begin();
            session.persist(livro);
            transaction.commit();
            lastCommitSuccess = true;
        } catch (HibernateException e) {
            if (transaction != null) transaction.rollback();
            lastErrorMessage = e.getLocalizedMessage();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
    
    /**
     * Retorna um livro a partir de sua <code>id</code>
     * @param id <code>id</code> do <code>Livro</code> para consultar
     * @return <code>Livro</code> encontrado.
     * @throws LivroNaoEncontradoException Indica quando um <code>Livro</code> não foi encontrado.
     */
    public Livro findLivro(Integer id) throws LivroNaoEncontradoException{
        Session session = factory.openSession();
        Transaction transaction = null;
        lastCommitSuccess = false;
        try {
            transaction = session.getTransaction();
            transaction.begin();
            
            Livro livro = (Livro) session.get(Livro.class, id);
            
            if (livro == null) throw new LivroNaoEncontradoException(
                    String.format("O livro id %d não foi encontrado.", id)
            );
            
            transaction.commit();
            lastCommitSuccess = true;
            
            return livro;
        } catch (HibernateException e) {
            if (transaction != null) transaction.rollback();
            lastErrorMessage = e.getLocalizedMessage();
            e.printStackTrace();
        } finally {
            session.close();
        }
        
        return null;
    }
    
    /**
     * Retorna um <code>List</code> de <code>Livro</code> a partir de parte do <code>titulo</code>
     * @param titulo Parte de um <code>titulo</code> do <code>Livro</code> para consultar
     * @return <code>List</code> de <code>Livro</code> encontrados
     * @throws LivroNaoEncontradoException Indica quando <code>Livro</code> não foi encontrado.
     */
    public List<Livro> findLivros(String titulo) throws LivroNaoEncontradoException {
        // Concatenando % para consulta de parte do nome
        StringBuilder tituloPerc = new StringBuilder();
        tituloPerc.append('%');
        tituloPerc.append(titulo);
        tituloPerc.append('%');
        
        Session session = factory.openSession();
        Transaction transaction = null;
        List result = null;
        lastCommitSuccess = false;
        try {
            transaction = session.getTransaction();
            transaction.begin();
            Criteria criteria = session.createCriteria(Livro.class);
            criteria.add(Restrictions.like("titulo", tituloPerc.toString()));
            
            if (!criteria.list().isEmpty()){
                result = criteria.list();
            }
            
            if (result == null) {
                throw new LivroNaoEncontradoException(String.format(
                        "Nenhum livro com titulo \"%s\" foi encontrado.", titulo)
                );
            }
            
            transaction.commit();
            lastCommitSuccess = true;
            
            return result;
        } catch (HibernateException e) {
            if (transaction != null) transaction.rollback();
            lastErrorMessage = e.getLocalizedMessage();
            e.printStackTrace();
        } finally {
            session.close();
        }
        
        return null;
    }
    
    /**
     * Retorna todos os livros cadastrados no banco.
     * @return <code>List</code> de todos os livros
     */
    public List<Livro> findLivros() throws LivroNaoEncontradoException {
        return findLivros(true, -1, -1);
    }
    
    /**
     * Retorna uma <code>List</code> de livros a partir de um intervalo.
     * @param primeiro A partir do número específico
     * @param maximo Máximo de resultados a retornar
     * @return <code>List</code> de <code>Livro</code>
     */
    public List<Livro> findLivros(int primeiro, int maximo) throws LivroNaoEncontradoException {
        return findLivros(false, primeiro, maximo);
    }
    
    private List<Livro> findLivros(boolean all, int primeiro, int maximo) throws LivroNaoEncontradoException {
        Session session = factory.openSession();
        Transaction transaction = null;
        List result = null;
        lastCommitSuccess = false;
        try {
            transaction = session.getTransaction();
            transaction.begin();
            Criteria criteria = session.createCriteria(Livro.class);
            
            if (!all) {
                criteria.setFirstResult(primeiro);
                criteria.setMaxResults(maximo);
            }
            
            if (!criteria.list().isEmpty()){
                result = criteria.list();
            }
            
            if (result == null) {
                throw new LivroNaoEncontradoException("Nenhum livro foi encontrado.");
            }
            
            transaction.commit();
            lastCommitSuccess = true;
            
            return criteria.list();
        } catch (HibernateException e) {
            if (transaction != null) transaction.rollback();
            lastErrorMessage = e.getLocalizedMessage();
            e.printStackTrace();
        } finally {
            session.close();
        }

        return null;
    }
    
    /**
     * Modifica um <code>Livro</code> cadastrado no banco
     * @param id <code>id</code> do <code>Livro</code> para modificar
     * @param livroEdit Dados para atualizar no banco
     */
    public void edit(Integer id, Livro livroEdit) throws LivroNaoEncontradoException {
        Session session = factory.openSession();
        Transaction transaction = null;
        lastCommitSuccess = false;
        try {
            transaction = session.getTransaction();
            transaction.begin();
            Livro livro = null;
            
            try {
                livro = findLivro(id);
            } catch (LivroNaoEncontradoException e) {
                lastErrorMessage = e.getLocalizedMessage();
                e.printStackTrace();
                throw(e);
            }
            
            livroEdit.setId(livro.getId());
            livroEdit.setCadastro(livro.getCadastro());
            
            session.update(livroEdit);
            transaction.commit();
            lastCommitSuccess = true;
        } catch (HibernateException e) {
            if (transaction != null) transaction.rollback();
            lastErrorMessage = e.getLocalizedMessage();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
    
    /**
     * Remove um livro do banco de dados a partir de sua <code>id</code>.
     * @param id <code>id</code> do <code>Livro</code> para remover do banco
     */
    public void destroy(Integer id) throws LivroNaoEncontradoException {
        Session session = factory.openSession();
        Transaction transaction = null;
        lastCommitSuccess = false;
        try {
            transaction = session.getTransaction();
            transaction.begin();
            Livro livro = null;
            
            try {
                livro = findLivro(id);
            } catch (LivroNaoEncontradoException e) {
                lastErrorMessage = e.getLocalizedMessage();
                e.printStackTrace();
                throw(e);
            }
            
            session.delete(livro);
            transaction.commit();
            lastCommitSuccess = true;
        } catch (HibernateException e) {
            if (transaction != null) transaction.rollback();
            lastErrorMessage = e.getLocalizedMessage();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
    
    /**
     * Retorna uma contagem de livros cadastrados no banco de dados.
     * @return Total de livros cadastrados
     */
    public int getLivrosCount() {
        Session session = factory.openSession();
        Transaction transaction = null;
        lastCommitSuccess = false;
        int total = 0;
        try {
            transaction = session.getTransaction();
            transaction.begin();
            total = session.createCriteria(Livro.class).list().size();
            transaction.commit();
            lastCommitSuccess = true;
        } catch (HibernateException e) {
            if (transaction != null) transaction.rollback();
            lastErrorMessage = e.getLocalizedMessage();
            e.printStackTrace();
        } finally {
            session.close();
        }
        
        return total;
    }
    
    public boolean isLastSuccess() {
        return lastCommitSuccess;
    }
    
    public String getLastErrorMessage() {
        return lastErrorMessage;
    }
}
