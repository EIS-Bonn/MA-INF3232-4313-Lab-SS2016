function Cy2NeoD3(config, graphId, tableId, transaction_hash ,urlSource, renderGraph, cbResult)
{


    var neod3 = new Neod3Renderer();
    var neo = new Neo(urlSource);

    try
    {


      var query =  "MATCH (n:BTC_Address)-[r:Inputs_to]->(m:Transaction) \n";
          query += "MATCH (m:Transaction)-[x:Outputs_to]->(p:BTC_Address) \n";
          query += "where p<>n AND m.hsh='"+transaction_hash+"' \n";
          query += "Optional MATCH (a:Person)-[h:HAS]->(p) \n";
          query += "RETURN n,r,m,x,p,a  \n";
          query += "LIMIT 50;  \n";

      // var query = editor.getValue();
      console.log("Executing Query:",query);
      var execButton = $(this).find('i');

      execButton.toggleClass('fa-play-circle-o fa-spinner fa-spin')

      neo.executeQuery(query,{},function(err,res)
      {
          execButton.toggleClass('fa-spinner fa-spin fa-play-circle-o')
          res = res || {}
          var graph=res.graph;

          if (renderGraph)
          {
              if (graph)
              {
                var c=$("#"+graphId);
                c.empty();
                neod3.render(graphId, c ,graph);
                renderResult(tableId, res.table);
              }
              else
              {
                  if (err)
                  {
                      console.log(err);

                      if (err.length > 0)
                      {
                        sweetAlert("Cypher error", err[0].code + "\n" + err[0].message, "error");
                      }
                      else
                      {
                        sweetAlert("Ajax " + err.statusText, "Status " + err.status + ": " + err.state(), "error");
                      }
                  }
              }
          }

          if(cbResult)
            cbResult(res);
       });

    }//end of try
    catch(e)
    {
      console.log(e);
      sweetAlert("Catched error", e, "error");
    }

    return false;


}//end of function
