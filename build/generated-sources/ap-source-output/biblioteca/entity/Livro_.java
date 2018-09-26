package biblioteca.entity;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Livro.class)
public abstract class Livro_ {

	public static volatile SingularAttribute<Livro, String> capa;
	public static volatile SingularAttribute<Livro, String> resenha;
	public static volatile SingularAttribute<Livro, Short> lancamento;
	public static volatile SingularAttribute<Livro, String> titulo;
	public static volatile SingularAttribute<Livro, Integer> id;
	public static volatile SingularAttribute<Livro, Date> cadastro;
	public static volatile SingularAttribute<Livro, Float> nota;
	public static volatile SingularAttribute<Livro, String> autor;

}

