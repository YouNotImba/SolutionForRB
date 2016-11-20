package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import dbutil.ConnectionManager;

@ManagedBean
@RequestScoped
public class BookManagedBean {
	private int id;
	private String title;
	private String author;

	public BookManagedBean() {

	}

	public BookManagedBean(int id, String title, String author) {
		super();
		this.id = id;
		this.title = title;
		this.author = author;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	private static Connection conn = null;
	private static PreparedStatement stmt = null;
	private static ResultSet rs = null;

	public ArrayList<BookManagedBean> getAllBooks() {

		ArrayList<BookManagedBean> allBooks = new ArrayList<>();

		try {
			conn = ConnectionManager.getInstance().getConnection();
			stmt = conn.prepareStatement("SELECT id,title,author FROM book");
			rs = stmt.executeQuery();
			while (rs.next()) {
				BookManagedBean bmb = new BookManagedBean();
				bmb.setId(rs.getInt("id"));
				bmb.setTitle(rs.getString("title"));
				bmb.setAuthor(rs.getString("author"));
				allBooks.add(bmb);
			}
		} catch (Exception e) {
			Logger.getLogger(BookManagedBean.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			try {
				conn.close();
				stmt.close();
				rs.close();
			} catch (SQLException e) {
				Logger.getLogger(BookManagedBean.class.getName()).log(Level.SEVERE, null, e);
			}
		}
		return allBooks;

	}

	public void Edit() {
		int idBook;
		ArrayList<BookManagedBean> books = getAllBooks();
		FacesContext fc = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) fc.getExternalContext().getRequest();
		idBook = Integer.parseInt(request.getParameter("id"));
		for (BookManagedBean bookManagedBean : books) {
			if (bookManagedBean.getId() == idBook) {
				this.setId(bookManagedBean.getId());
				this.setTitle(bookManagedBean.getTitle());
				this.setAuthor(bookManagedBean.getAuthor());
			}

		}

	}

	public void add() {
		try {
			conn = ConnectionManager.getInstance().getConnection();
			stmt = conn.prepareStatement("INSERT into book (title,author) VALUES(?,?)");
			stmt.setString(1, this.getTitle());
			stmt.setString(2, this.getAuthor());
			int affected = stmt.executeUpdate();
			if (affected > 0) {
				System.out.println("success");
			}

		} catch (Exception e) {
			Logger.getLogger(BookManagedBean.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			try {
				conn.close();
				stmt.close();

			} catch (SQLException e) {
				Logger.getLogger(BookManagedBean.class.getName()).log(Level.SEVERE, null, e);
			}
		}

	}

	public void update() {
		FacesContext fc = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) fc.getExternalContext().getRequest();
		int idBook = Integer.parseInt(request.getParameter("id"));
		try {
			conn = ConnectionManager.getInstance().getConnection();
			stmt = conn.prepareStatement("UPDATE book SET title=?,author=? WHERE id=?");
			stmt.setString(1, this.getTitle());
			stmt.setString(2, this.getAuthor());
			stmt.setInt(3, idBook);
			int affected = stmt.executeUpdate();
			if (affected > 0) {
				System.out.println("success");
			}

		} catch (Exception e) {
			Logger.getLogger(BookManagedBean.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			try {
				conn.close();
				stmt.close();

			} catch (SQLException e) {
				Logger.getLogger(BookManagedBean.class.getName()).log(Level.SEVERE, null, e);
			}
		}

	}

	public void delete() {
		FacesContext fc = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) fc.getExternalContext().getRequest();
		System.out.println("delete id parameter is " + request.getParameter("id"));
		int idBook = Integer.parseInt(request.getParameter("id"));
		try {
			conn = ConnectionManager.getInstance().getConnection();
			stmt = conn.prepareStatement("DELETE FROM book WHERE id = ?");
			stmt.setInt(1, idBook);
			int affected = stmt.executeUpdate();
			if (affected > 0) {
				System.out.println("success");
			}

		} catch (Exception e) {
			Logger.getLogger(BookManagedBean.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			try {
				conn.close();
				stmt.close();

			} catch (SQLException e) {
				Logger.getLogger(BookManagedBean.class.getName()).log(Level.SEVERE, null, e);
			}
		}

	}

}
