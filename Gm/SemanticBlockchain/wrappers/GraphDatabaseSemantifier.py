import os
import json
import requests
import py2neo


class GraphDatabaseSemantifier:
    RDFns = "http://www.w3.org/1999/02/22-rdf-syntax-ns#"
    RDFsNS = "http://www.w3.org/2000/01/rdf-schema#"
    OWLns = "http://www.w3.org/2002/07/owl#"
    SchemaOrgns = "http://schema.org#"
    GraphDatabasePrefix = "http://semanticblockchain.com#"

    def create(self, URL, Auth):
        # type: (object, object) -> object
        py2neo.authenticate(URL, Auth[0], Auth[1])
        neo4jUrl = os.environ.get('NEO4J_URL', "http://" + URL + "/db/data/")
        graph = py2neo.Graph(neo4jUrl)

        # Adding RDF Nodes
    # rdf:Property
        query = """
        MERGE (rdf:RDF {label:"rdf:Property", name:"Property", prefix:"rdf"}) ON CREATE
        SET rdf.ns ={ns}
        """
        # Send Cypher query.
        graph.run(query, ns=self.RDFns)

    # rdf:type
        query = """
        MERGE (rdf:RDF {label:"rdf:type", name:"type", prefix:"rdf"}) ON CREATE
        SET rdf.ns ={ns}
        """
        # Send Cypher query.
        graph.run(query, ns=self.RDFns)

    # rdf:type rdf:type rdf:property
        query = """
        MATCH (p:RDF {name:"type"}), (b:RDF {name:"Property"})
        MERGE (p)-[:type{label:"rdf:type",ns:"{ns}"}]->(b)
                """
        # Send Cypher query.
        graph.run(query, ns=self.RDFns)
        ##############################################
        # Adding RDFs Nodes

        # rdfs:subclassof
        query = """
                   MERGE (rdfs:RDFs {label:"rdfs:subClassOf", name:"subClassOf", prefix:"rdfs"}) ON CREATE
                   SET rdfs.ns ={ns}
                   """

        # Send Cypher query.
        graph.run(query, ns=self.RDFsNS)

        # rdfs:domain
        query = """
                MERGE (rdfs:RDFs {label:"rdfs:domain", name:"domain", prefix:"rdfs"}) ON CREATE
                SET rdfs.ns ={ns}
                           """

        # rdfs:range
        query = """
        MERGE (rdfs:RDFs {label:"rdfs:range", name:"range", prefix:"rdfs"}) ON CREATE
        SET rdfs.ns ={ns}
        """

        # Send Cypher query.
        graph.run(query, ns=self.RDFsNS)
        #########################################
        # Adding OWL Nodes
        # owl:Thing
        query = """
        MERGE (owl:OWL {label:"owl:Thing", name:"Thing", prefix:"owl"}) ON CREATE
        SET owl.ns ={ns}
        """
        # Send Cypher query.
        graph.run(query, ns=self.OWLns)

        # owl:Ontology
        query = """
        MERGE (owl:OWL {label:"owl:Ontology", name:"Ontology", prefix:"owl"}) ON CREATE
        SET owl.ns ={ns}
        """
        # Send Cypher query.
        graph.run(query, ns=self.OWLns)
        # owl:Ontology rdfs:SubclassOf owl:Thing
        query = """
        MATCH (p:OWL {name:"Ontology"}), (b:OWL {name:"Thing"})
        MERGE (p)-[:subClassOf{label:"rdfs:subClassOf",ns:"{ns}"}]->(b)
        """
        # Send Cypher query.
        graph.run(query, ns=self.RDFsNS)

        # owl:class
        query = """
        MERGE (owl:OWL {label:"owl:Class", name:"Class", prefix:"owl"}) ON CREATE
        SET owl.ns ={ns}
        """
        # Send Cypher query.
        graph.run(query, ns=self.OWLns)

        # owl:ObjectProperty
        query = """
                     MERGE (owl:OWL {label:"owl:ObjectProperty", name:"ObjectProperty", prefix:"owl"}) ON CREATE
                     SET owl.ns ={ns}
                     """
        # Send Cypher query.
        graph.run(query, ns=self.OWLns)

        # owl:equivalentClass
        query = """
                MERGE (owl:OWL {label:"owl:equivalentClass", name:"equivalentClass", prefix:"owl"}) ON CREATE
                SET owl.ns ={ns}
                """
        # Send Cypher query.
        graph.run(query, ns=self.OWLns)

        # owl:equivalentClass rdf:type owl:ObjectProperty
        query = """
        MATCH (p:OWL {name:"equivalentClass"}), (b:OWL {name:"ObjectProperty"})
        MERGE (p)-[:type{label:"rdf:type",ns:"{ns}"}]->(b)
        """
        # Send Cypher query.
        graph.run(query, ns=self.RDFns)

        #           # owl:disjointWith
        query = """
                      MERGE (owl:OWL {label:"owl:disjointWith", name:"disjointWith", prefix:"owl"}) ON CREATE
                      SET owl.ns ={ns}
                      """
        # Send Cypher query.
        graph.run(query, ns=self.OWLns)

        # owl:disjointWith rdf:type owl:ObjectProperty
        query = """
        MATCH (p:OWL {name:"disjointWith"}), (b:OWL {name:"ObjectProperty"})
        MERGE (p)-[:type{label:"rdf:type",ns:"{ns}"}]->(b)
        """
        # Send Cypher query.
        graph.run(query, ns=self.RDFns)

        #            # owl:equivalentProperty
        query = """
        MERGE (owl:OWL {label:"owl:equivalentProperty", name:"equivalentProperty", prefix:"owl"}) ON CREATE
        SET owl.ns ={ns}
        """
        # Send Cypher query.
        graph.run(query, ns=self.OWLns)

        # owl:equivalentProperty rdf:type owl:ObjectProperty
        query = """
        MATCH (p:OWL {name:"equivalentProperty"}), (b:OWL {name:"ObjectProperty"})
        MERGE (p)-[:type{label:"rdf:type",ns:"{ns}"}]->(b)
                                     """
        # Send Cypher query.
        graph.run(query, ns=self.RDFns)

        #            # owl:inverseOf
        query = """
        MERGE (owl:OWL {label:"owl:inverseOf", name:"inverseOf", prefix:"owl"}) ON CREATE
        SET owl.ns ={ns}
        """
        # Send Cypher query.
        graph.run(query, ns=self.OWLns)

        # owl:inverseOf rdf:type owl:ObjectProperty
        query = """
        MATCH (p:OWL {name:"inverseOf"}), (b:OWL {name:"ObjectProperty"})
        MERGE (p)-[:type{label:"rdf:type",ns:"{ns}"}]->(b)
        """
        # Send Cypher query.
        graph.run(query, ns=self.RDFns)

        #             # owl:SameAs
        query = """
                MERGE (owl:OWL {label:"owl:SameAs", name:"SameAs", prefix:"owl"}) ON CREATE
                SET owl.ns ={ns}
                """
        # Send Cypher query.
        graph.run(query, ns=self.OWLns)

        # owl:SameAs rdf:type owl:ObjectProperty
        query = """
                MATCH (p:OWL {name:"SameAs"}), (b:OWL {name:"ObjectProperty"})
                MERGE (p)-[:type{label:"rdf:type",ns:"{ns}"}]->(b)
                """
        # Send Cypher query.
        graph.run(query, ns=self.RDFns)

        #               # owl:TransitiveProperty
        query = """
                MERGE (owl:OWL {label:"owl:TransitiveProperty", name:"TransitiveProperty", prefix:"owl"}) ON CREATE
                SET owl.ns ={ns}
                """
        # Send Cypher query.
        graph.run(query, ns=self.OWLns)

        # owl:TransitiveProperty rdf:type owl:ObjectProperty
        query = """
                MATCH (p:OWL {name:"TransitiveProperty"}), (b:OWL {name:"ObjectProperty"})
                MERGE (p)-[:type{label:"rdf:type",ns:"{ns}"}]->(b)
                """
        # Send Cypher query.
        graph.run(query, ns=self.RDFns)
        ######################
        # Adding scheme.org/Person (vocab:Person)
        query = """
                      MERGE (Person:Schema_org {label:"sc:Person", name:"Person", prefix:"vocab"}) ON CREATE
                      SET Person.ns ={ns}
                      """
        # Send Cypher query.
        graph.run(query, ns=self.SchemaOrgns)

        # vocab:Person rdf:type owl:Class
        query = """
        MATCH (p:Schema_org {name:"Person"}), (b:OWL {name:"Class"})
        MERGE (p)-[:type{label:"rdf:type",ns:"{ns}"}]->(b)
        """
        # Send Cypher query.
        graph.run(query, ns=self.RDFns)
        ######################
        # Adding Blondie
        #            #  Bldy:Blondie
        query = """
        MERGE (Blondie:Blondie {label:"Bldy:Blondie", name:"Blondie", prefix:"Bldy"}) ON CREATE
        SET Blondie.ns ="http://www.semanticweb.org/hector/ontologies/2016/5/Blondie#"
        """
        # Send Cypher query.
        graph.run(query)

        # Bldy:Blondie rdf:type owl:Ontology
        query = """
                          MATCH (p:Blondie {name:"Blondie"}), (b:OWL {name:"Ontology"})
                          MERGE (p)-[:type{label:"rdf:type",ns:"{ns}"}]->(b)
                          """
        # Send Cypher query.
        graph.run(query, ns=self.RDFns)

        # Blondie Transaction
        query = """
                MERGE (Transaction:Blondie {label:"Bldy:Transaction", name:"Transaction", prefix:"Bldy"}) ON CREATE
                SET Transaction.ns ="http://www.semanticweb.org/hector/ontologies/2016/5/Blondie#"
                """
        # Send Cypher query.
        graph.run(query)

        # Bldy:Transaction rdf:type owl:Class
        query = """
        MATCH (p:Blondie {name:"Transaction"}), (b:OWL {name:"Class"})
        MERGE (p)-[:type{label:"rdf:type",ns:"{ns}"}]->(b)
        """
        # Send Cypher query.
        graph.run(query, ns=self.RDFns)

        # Blondie BitcoinTransaction
        query = """
        MERGE (BitcoinTransaction:Blondie {label:"Bldy:BitcoinTransaction", name:"BitcoinTransaction", prefix:"Bldy"}) ON CREATE
        SET BitcoinTransaction.ns ="http://www.semanticweb.org/hector/ontologies/2016/5/Blondie#"
                """
        # Send Cypher query.
        graph.run(query)

        # Bldy:BitcoinTransaction rdf:type owl:Class
        query = """
        MATCH (p:Blondie {name:"BitcoinTransaction"}), (b:OWL {name:"Class"})
        MERGE (p)-[:type{label:"rdf:type",ns:"{ns}"}]->(b)
        """
        # Send Cypher query.
        graph.run(query, ns=self.RDFns)

        # Bldy:BitcoinTransaction rdfs:SubclassOf Bldy:Transaction
        query = """
        MATCH (p:Blondie {name:"BitcoinTransaction"}), (b:Blondie {name:"Transaction"})
        MERGE (p)-[:subClassOf{subClassOf:"rdfs:subClassOf",ns:"{ns}"}]->(b)
        """
        # Send Cypher query.
        graph.run(query, ns=self.RDFsNS)

        ###############
        #  Extending Blondie
        ###############
    # Sabo Transaction
        query = """
        MERGE (Transaction:Sabo {label:"sb:Transaction", name:"Transaction", prefix:"sb"}) ON CREATE
        SET Transaction.ns ="{ns}"
        """
        # Send Cypher query.
        graph.run(query, ns=self.GraphDatabasePrefix)

    # sb:Transaction rdf:type owl:Class
        query = """
            MATCH (p:Sabo {name:"Transaction"}), (b:OWL {name:"Class"})
            MERGE (p)-[:type{label:"rdf:type",ns:"{ns}"}]->(b)
            """
        # Send Cypher query.
        graph.run(query, ns=self.RDFns)

    # sb:Transaction rdfs:equivalentClass Bldy:BitcoinTransaction
        query = """
            MATCH (p:Sabo {name:"Transaction"}), (b:Blondie {name:"BitcoinTransaction"})
            MERGE (p)-[:equivalentClass{label:"owl:equivalentClass",ns:"{ns}"}]->(b)
            """
        # Send Cypher query.
        graph.run(query, ns=self.OWLns)

    # Sabo BTC_Address

        query = """
                MERGE (BTC_Address:Sabo {label:"sb:BTC_Address", name:"BTC_Address", prefix:"sb"}) ON CREATE
                SET BTC_Address.ns ="{ns}"
                """
        # Send Cypher query.
        graph.run(query, ns=self.GraphDatabasePrefix)

    # sb:BTC_Address rdf:type owl:Class
        query = """
                    MATCH (p:Sabo {name:"BTC_Address"}), (b:OWL {name:"Class"})
                    MERGE (p)-[:type{label:"rdf:type",ns:"{ns}"}]->(b)
                    """
        # Send Cypher query.
        graph.run(query, ns=self.RDFns)

    # Sabo Transfer
        query = """
        MERGE (Transfer:Sabo {label:"sb:Transfer", name:"Transfer", prefix:"sb"}) ON CREATE
        SET Transfer.ns ="{ns}"
        """
        # Send Cypher query.
        graph.run(query, ns=self.GraphDatabasePrefix)

    # sb:Transaction rdf:type owl:ObjectProperty
        query = """
            MATCH (p:Sabo {name:"Transfer"}), (b:OWL {name:"ObjectProperty"})
            MERGE (p)-[:type{label:"rdf:type",ns:"{ns}"}]->(b)
            """
        # Send Cypher query.
        graph.run(query, ns=self.RDFns)
    # Transfer range
        query = """
                   MATCH (p:Sabo {name:"Transfer"}), (b:Sabo {name:"Transaction"})
                   MERGE (p)-[:range{label:"rdfs:range",ns:"{ns}"}]->(b)
                   """
        # Send Cypher query.
        graph.run(query, ns=self.RDFsNS)

    # Transfer domain
        query = """
                           MATCH (p:Sabo {name:"Transfer"}), (b:Sabo {name:"BTC_Address"})
                           MERGE (p)-[:domain{label:"rdfs:domain",ns:"{ns}"}]->(b)
                           """
        # Send Cypher query.
        graph.run(query, ns=self.RDFsNS)

    # Sabo Receive
        query = """
        MERGE (Receive:Sabo {label:"sb:Receive", name:"Receive", prefix:"sb"}) ON CREATE
        SET Receive.ns ="{ns}"
        """
        # Send Cypher query.
        graph.run(query, ns=self.GraphDatabasePrefix)

    # sb:Transaction rdf:type owl:ObjectProperty
        query = """
            MATCH (p:Sabo {name:"Receive"}), (b:OWL {name:"ObjectProperty"})
            MERGE (p)-[:type{Label:"rdf:type",ns:"{ns}"}]->(b)
            """
        # Send Cypher query.
        graph.run(query, ns=self.RDFns)

    # sb:Transfer owl:inverseOf sb:Receive
        query = """
            MATCH (p:Sabo {name:"Transfer"}), (b:Blondie {name:"Receive"})
            MERGE (p)-[:inverseOf{Label:"owl:inverseOf",ns:"{ns}"}]->(b)
            """
        # Send Cypher query.
        graph.run(query, ns=self.OWLns)

    # Receive range

        query = """
                   MATCH (p:Sabo {name:"Receive"}), (b:Sabo {name:"BTC_Address"})
                   MERGE (p)-[:range{Label:"rdfs:range",ns:"{ns}"}]->(b)
                   """
        # Send Cypher query.
        graph.run(query, ns=self.RDFsNS)

    # Receive domain

        query = """
        MATCH (p:Sabo {name:"Receive"}), (b:Sabo {name:"Transaction"})
        MERGE (p)-[:domain{Label:"rdfs:domain",ns:"{ns}"}]->(b)
        """
        # Send Cypher query.
        graph.run(query, ns=self.RDFsNS)

    # vocab:Person sb:Own  sb:BTC_Address
        query = """
            MATCH (p:Schema_org {name:"Person"}), (b:Sabo {name:"BTC_Address"})
            MERGE (p)-[:Own{label:"sb:Own",ns:"{ns}"}]->(b)
            """
        # Send Cypher query.
        graph.run(query, ns=self.GraphDatabasePrefix)

    # sb:Twitter Account
        query = """
                MERGE (Transfer:Sabo {label:"sb:TwitterAccount", name:"TwitterAccount", prefix:"sb"}) ON CREATE
                SET Transfer.ns ="{ns}"
                """
        # Send Cypher query.
        graph.run(query, ns=self.GraphDatabasePrefix)

    # sb:TwitterAccount rdf:type owl:Class
        query = """
                    MATCH (p:Sabo {name:"TwitterAccount"}), (b:OWL {name:"Class"})
                    MERGE (p)-[:type{label:"rdf:type",ns:"{ns}"}]->(b)
                    """
        # Send Cypher query.
        graph.run(query, ns=self.RDFns)

    # vocab:Person sb:Has  sb:TwitterAccount
        query = """
                    MATCH (p:Schema_org {name:"Person"}), (b:Sabo {name:"TwitterAccount"})
                    MERGE (p)-[:Has{label:"sb:Has",ns:"{ns}"}]->(b)
                    """
        # Send Cypher query.
        graph.run(query, ns=self.GraphDatabasePrefix)



    # sb:follows
        query = """
                    MERGE (follows:Sabo {label:"sb:follows", name:"follows", prefix:"sb"}) ON CREATE
                    SET follows.ns ="{ns}"
                    """
        # Send Cypher query.
        graph.run(query, ns=self.GraphDatabasePrefix)

    # sb:follows type ObjectProperty
        query = """
                MATCH (p:Sabo {name:"follows"}), (b:OWL {name:"ObjectProperty"})
                MERGE (p)-[:type{label:"rdf:type",ns:"{ns}"}]->(b)
                """
        # Send Cypher query.
        graph.run(query, ns=self.RDFns)

    # sb:follows range
        query = """
         MATCH (p:Sabo {name:"follows"}), (b:Sabo {name:"TwitterAccount"})
         MERGE (p)-[:range{label:"rdfs:range",ns:"{ns}"}]->(b)
         """
        # Send Cypher query.
        graph.run(query, ns=self.RDFsNS)

    # sb:follow Domain
        query = """
        MATCH (p:Sabo {name:"follows"}), (b:Sabo {name:"TwitterAccount"})
        MERGE (p)-[:domain{label:"rdfs:domain",ns:"{ns}"}]->(b)
        """
        # Send Cypher query.
        graph.run(query, ns=self.RDFsNS)



