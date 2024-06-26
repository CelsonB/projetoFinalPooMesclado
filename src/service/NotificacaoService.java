package service;

import java.awt.Toolkit;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Timer;

import javax.swing.JOptionPane;

import dao.BancoDados;
import dao.CompromissoDAO;

import entities.Compromisso;
import entities.Usuario;

public class NotificacaoService  implements Runnable{

	
	
	 private Timer timer;
	 private Usuario sessao;
	 private Date currentDate;
	  

	 public NotificacaoService(Usuario sessao) {
		 this.sessao = sessao;
	 }

	@Override
	public void run() {
		
	
			
			
		
			
			
			
			while(true) {
				try(	Connection conn = BancoDados.conectar())
				{
					CompromissoDAO compromissoDao = new CompromissoDAO(conn);
					List<Compromisso> compromissos = compromissoDao.verificarCompromissos(sessao);
					if(compromissos!=null) for(Compromisso comp : compromissos) {
							
							 		Toolkit.getDefaultToolkit().beep();	
							
								
								 int resposta = JOptionPane.showConfirmDialog(null,
					                        "Você tem um compromisso as " + comp.getDataInicio() + " Confirma?",
					                        "Notificação de Compromisso",
					                        JOptionPane.YES_NO_OPTION);

					                if (resposta == JOptionPane.YES_OPTION) {
			                            try (Connection conn2 = BancoDados.conectar()) { 
			                                CompromissoDAO compromissoDao2 = new CompromissoDAO(conn2);
			                                compromissoDao2.excluirCompromisso(comp.getIdCompromisso());
			                            }

					                
					                	
					                } else {
					                    
					                }
					                	Toolkit.getDefaultToolkit().beep();	
								
					                	
							
							
						}
						
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 try {
	  	                Thread.sleep(60000); 
	  	            } catch (InterruptedException e) {
	  	                e.printStackTrace();
	  	            }	
				
			}
				
			 	
	}
}
