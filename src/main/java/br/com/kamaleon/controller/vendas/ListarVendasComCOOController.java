package br.com.kamaleon.controller.vendas;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import br.com.kamaleon.service.UtilService;
import br.com.kamaleon.service.vendas.VendasService;
import br.com.kamaleon.util.Constantes;

@Controller
@RequestMapping(value="/listarVendasComCOO")
public class ListarVendasComCOOController {
	
	@Autowired
	private VendasService service;
		
	@Autowired
	private UtilService utilService;
	
	@ModelAttribute(value="listaEstabelecimento")
	public List<Object[]> getListaEstabelecimento() {
						
		return utilService.listarEstabelecimentosAtivos();
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public ModelAndView list(){		
		ModelAndView mav = new ModelAndView("/vendas/listarVendasComCOO");		
		return mav;
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/json")
	@ResponseBody()
	public List<Object[]> listar(@RequestParam("loja") String loja, @RequestParam("dataInicial") String dataInicial, @RequestParam("dataFinal") String dataFinal)  {
        
		return service.listarVendasComCOO(loja, dataInicial, dataFinal);
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/xls")
	@ResponseBody()
	private String gerarArquivoExportacaoXls(@RequestParam("loja") String loja, @RequestParam("dataInicial") String dataInicial, @RequestParam("dataFinal") String dataFinal)
			throws Exception {
		
		String nomeArquivo = "VendasComCOO.xls";
		
		File arquivo = new File(Constantes.caminhoArquivosXLS + File.separator + nomeArquivo);
		
		String url = arquivo.getAbsolutePath(); 
		List<Object[]> lista = service.listarVendasComCOO(loja, dataInicial, dataFinal);

		String[] colunas = new String[]{ 
				"ID_LOJA",
				"NM_LOJA",
				"DS_SIGLA_LOJA",
				"ID_CLIENTE",
				"NU_CPF_CNPJ",
				"NM_CLIENTE",
				"ID_VENDA",
				"DT_VENDA",
				"DT_REFERENCIA",
				"NU_CUPOM",
				"VL_VENDA_LIQUIDA",
				"DS_ORCAMENTO",
				"DS_FORMA_PAGAMENTO",
				"NU_SERIE_IMPRESSORA",
				"VL_VENDA_EXIBIVEL"
				};
		
	    HSSFWorkbook wb = new HSSFWorkbook();
	    HSSFSheet plan1 = null;
	    FileOutputStream file = null;
	    
	    try {

	      file = new FileOutputStream(new File(url));
	      plan1 = wb.createSheet("Plan1");
	      HSSFRow row = null;
	      int linha = 0;
	      boolean imprimiuColunas = false;
	      for (Object[] coluna : lista) {
	        row = plan1.createRow(linha);
	        if (!imprimiuColunas) {
	          for (int i = 0; i < colunas.length; i++) {
	            row.createCell(i).setCellValue(colunas[i]);
	            imprimiuColunas = true;
	          }
	          linha++;
	          row = plan1.createRow(linha);
	        }
	        linha++;
	        row.createCell(0).setCellValue(coluna[0]+"");
	        row.createCell(1).setCellValue(coluna[1]+"");
	        row.createCell(2).setCellValue(coluna[2]+"");
	        row.createCell(3).setCellValue(coluna[3]+"");
	        row.createCell(4).setCellValue(coluna[4]+"");
	        row.createCell(5).setCellValue(coluna[5]+"");
	        row.createCell(6).setCellValue(coluna[6]+"");
	        row.createCell(7).setCellValue(coluna[7]+"");
	        row.createCell(8).setCellValue(coluna[8]+"");
	        row.createCell(9).setCellValue(coluna[9]+"");
	        row.createCell(10).setCellValue(coluna[10]+"");
	        row.createCell(11).setCellValue(coluna[11]+"");
	        row.createCell(12).setCellValue(coluna[12]+"");
	        row.createCell(13).setCellValue(coluna[13]+"");
	        row.createCell(14).setCellValue(coluna[14]+"");
	      }
	      wb.write(file);

	    } catch (Exception e) {
	      // TODO: handle exception
	    } finally {
	      if (file != null)
	        file.close();
	    }

	    return "/adm-oscar/xls/"+nomeArquivo;
	  }
}