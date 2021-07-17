/*
 * Copyright 2021 Cleuson Santos <cleusonss@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.inf.cs.dao;

import br.inf.cs.data.DataSource;
import br.inf.cs.logging.Logger;
import br.inf.cs.model.ProdutoAbcFarma;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductDao {

    public Integer setPMC17(ProdutoAbcFarma produtoAbcFarma){
        StringBuilder sb = new StringBuilder();
        sb.append("update produto_pmc ");
        sb.append("set ");
        sb.append("preco_maximo_consumidor = %s, ");
        sb.append("ultima_alteracao = GETDATE() ");
        sb.append("where ");
        sb.append("prod_codigo = ");
        sb.append("(select distinct codigo from produto where codigo_barra = '%s') ");
        sb.append("and ");
        sb.append(" aliquota = 17;");

        String update = String.format(sb.toString(), produtoAbcFarma.getPMC_17(), produtoAbcFarma.getEAN());
        Integer rows = getRows(produtoAbcFarma);
        if(rows == 1){
            return DataSource.executeUpdate(update);
        }else{
            if(rows > 1) {
                Logger.warning(this.getClass(), "Error: EAN " + produtoAbcFarma.getEAN() + "possui " + rows + "registros");
            }
            return 0;
        }
    }

    public Integer setPF17(ProdutoAbcFarma produtoAbcFarma){
        StringBuilder sb = new StringBuilder();
        sb.append("update produto ");
        sb.append("set ");
        sb.append("preco_fabricante = %s, ");
        sb.append("data_ultima_alteracao = GETDATE() ");
        sb.append("where ");
        sb.append("codigo_barra = '%s'");
        sb.append("and ");
        sb.append(" aliquota_abc_farma = 17;");

        String update = String.format(sb.toString(), produtoAbcFarma.getPF_17(), produtoAbcFarma.getEAN());
        Integer rows = getRows(produtoAbcFarma);
        if(rows == 1){
            return DataSource.executeUpdate(update);
        }
        else{
            if(rows > 1) {
                Logger.warning(this.getClass(), "Error: EAN " + produtoAbcFarma.getEAN() + "possui " + rows + "registros");
            }
            return 0;
        }
    }

    public Integer getRows(ProdutoAbcFarma produtoAbcFarma){
        StringBuilder sb = new StringBuilder();
        sb.append("select distinct codigo ");
        sb.append("from produto ");
        sb.append("where ");
        sb.append("codigo_barra = '%s'");

        String query = String.format(sb.toString(), produtoAbcFarma.getEAN());
        
        try{
            ResultSet rs = DataSource.executeQuery(query);
            if (rs != null) {
                int i = 0;
                while(rs.next()){
                    i++;
                }
                return i;
            }
            return 0;
        }catch (SQLException e){
            Logger.warning(this.getClass(), "Error: " + e.getMessage());
            return 0;
        }
    }
}