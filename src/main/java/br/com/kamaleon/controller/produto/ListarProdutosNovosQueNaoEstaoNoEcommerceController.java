package br.com.kamaleon.controller.produto;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import br.com.kamaleon.service.produto.ProdutoService;
import br.com.kamaleon.util.Constantes;

@Controller
@RequestMapping(value="/listarProdutosNovosQueNaoEstaoNoEcommerce")
public class ListarProdutosNovosQueNaoEstaoNoEcommerceController {
	
	@Autowired
	private ProdutoService service;
		
	@RequestMapping(method=RequestMethod.GET)
	public ModelAndView list(){		
		ModelAndView mav = new ModelAndView("/produto/listarProdutosNovosQueNaoEstaoNoEcommerce");		
		return mav;
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/json")
	@ResponseBody()
	public List<Object[]> listar(@RequestParam("codigoProduto") String codigoProduto, @RequestParam("dataInicial") String dataInicial, @RequestParam("dataFinal") String dataFinal)  {
        
		return service.listarProdutosNovosQueNaoEstaoNoEcommerce(codigoProduto, dataInicial, dataFinal);
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/xls")
	@ResponseBody()
	private String gerarArquivoExportacaoXls(@RequestParam("codigoProduto") String codigoProduto, @RequestParam("dataInicial") String dataInicial, @RequestParam("dataFinal") String dataFinal)
			throws Exception {
		
		String nomeArquivo = "ProdutosNovosQueNaoEstaoNoEcommerce.xls";
		
		File arquivo = new File(Constantes.caminhoArquivosXLS + File.separator + nomeArquivo);
		
		String url = arquivo.getAbsolutePath(); 
		List<Object[]> lista = service.listarProdutosNovosQueNaoEstaoNoEcommerce(codigoProduto, dataInicial, dataFinal);

		String[] colunas = new String[]{ 
				"ID_PRODUTO",
				 "DT_KARDEX",
				  "NM_PRODUTO",
				  "NM_PRODUTO_EXIBIVEL",
				  "DS_REFERENCIA",
				  "ID_MARCA",
				  "NM_MARCA",
				  "NM_IMPRESSAO",
				  "NM_IMPRESSAO_CORRETO"
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