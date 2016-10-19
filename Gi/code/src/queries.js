import _ from 'lodash';


export default {
  /*
   * Main query which is spawned on loading of a new dataset.
   * Retrieves all the relevant data for the processes of certain
   * types which were loaded from the dataset.
   */
  ALL: `
    SELECT DISTINCT ?p ?pType ?supplier ?pn ?st ?et ?lat ?long ?name ?type {
      {
        ?pType rdfs:subClassOf scor:Deliver;
          rdfs:label ?label.
      } UNION {
        ?pType rdfs:subClassOf scor:Source;
          rdfs:label ?label.
      } UNION {
        ?pType rdfs:subClassOf scor:Make;
          rdfs:label ?label.
      }
      ?p a ?pType;
        ex:hasSupplier ?supplier;
        ex:isSubjectOf ?pn;
        ex:hasStartTime ?st;
        ex:hasEndTime ?et;
        ex:hasPath/ngeo:posList ?l.
      ?l rdfs:member ?mem.
      ?mem geo:lat ?lat;
        geo:long ?long;
        ex:hasName ?name;
        ex:hasType ?type.

    }`,
  /*
   * A base template for metric analysis which will
   * be extended depending on the metric and filters.
   */
  BASE: _.template(`
    SELECT ?p <%= select %>
    WHERE {
      ?p a scor:<%= processType %> .
      <%= triples %>
      <%= filters %>
    }
    GROUP BY ?p
    `),

  /*
   * Definition of metrics. They're defined  by a select part
   * which goes into the select part of base template. It must
   * return the result in the value ?metricResult.
   * Triples get the metric values that are required to compute
   * a certain analysis metric. Naming of the variables should
   * be consistent with those used in the select part.
   */
  METRICS: {
    DELIVERY_PERFORMANCE: {
      SELECT: '(AVG(xsd:decimal((xsd:decimal(?value1)+xsd:decimal(?value2))/2)) AS ?metricResult)',
      TRIPLES: [
        '?p scor:hasMetricRL_32 ?value1 .',
        '?p scor:hasMetricRL_34 ?value2 .',
      ],
    },
    DELIVERY_IN_FULL: {
      SELECT: '(AVG(xsd:decimal((xsd:decimal(?value1)+xsd:decimal(?value2))/2)) AS ?metricResult)',
      TRIPLES: [
        '?p scor:hasMetricRL_33 ?value1 .',
        '?p scor:hasMetricRL_50 ?value2 .',
      ],
    },
    PERFECT_CONDITION: {
      SELECT: '(AVG(xsd:double((xsd:decimal(?value1)+xsd:decimal(?value2)+xsd:decimal(?value3)+xsd:decimal(?value4)+xsd:decimal(?value5))/5)) AS ?metricResult)',
      TRIPLES: [
        '?p scor:hasMetricRL_12 ?value1 .',
        '?p scor:hasMetricRL_24 ?value2 .',
        '?p scor:hasMetricRL_41 ?value3 .',
        '?p scor:hasMetricRL_42 ?value4 .',
        '?p scor:hasMetricRL_55 ?value5 .',
      ],
    },
    PRODUCTION_COST: {
      SELECT: '(SUM(xsd:decimal(?value1)+xsd:decimal(?value2)+xsd:decimal(?value3)+xsd:decimal(?value4)) AS ?metricResult)',
      TRIPLES: [
        '?p scor:hasMetricCO_14 ?value1 .',
        '?p scor:hasMetricCO_15 ?value2 .',
        '?p scor:hasMetricCO_16 ?value3 .',
        '?p scor:hasMetricCO_17 ?value4 .',
      ],
    },
    UPSIDE_SUPPLY_CHAIN_FLEXIBILITY: {
      SELECT: '(SUM(xsd:decimal(?value1)+xsd:decimal(?value2)+xsd:decimal(?value3)+xsd:decimal(?value4)+xsd:decimal(?value5)) AS ?metricResult)',
      TRIPLES: [
        '?p scor:hasMetricAG_1 ?value1 .',
        '?p scor:hasMetricAG_2 ?value2 .',
        '?p scor:hasMetricAG_3 ?value3 .',
        '?p scor:hasMetricAG_4 ?value4 .',
        '?p scor:hasMetricAG_5 ?value5 .',
      ],
    },
    SOURCING_COST: {
      SELECT: '(SUM(xsd:double(xsd:decimal(?value1) + xsd:decimal(?value2) + xsd:decimal(?value3) + xsd:decimal(?value4))) AS ?metricResult)',
      TRIPLES: [
        '?p scor:hasMetricCO_5 ?value1 .',
        '?p scor:hasMetricCO_6 ?value2 .',
        '?p scor:hasMetricCO_7 ?value3 .',
        '?p scor:hasMetricCO_8 ?value4 .',
      ],
    },
  },
  /*
   * In the analysis query, the filter values must be considered.
   * This happens by extending the base template with values which
   * the user entered.
   * As with metrics, props (filters) provide a triples array which
   * contains some variables that are in turn used to filter the result.
   * In contrast to metrics, in props, the variables within triples are not
   * returned.
   * Filters array contains SPARQL filters to limit the selection of processes
   * according to the values entered by the user.
   */
  PROPS: {
    SUPPLIER: {
      TRIPLES: ['?p ex:hasSupplier ?supplier .'],
      FILTERS: _.template('FILTER(regex(str(?supplier), "<%= supplier %>")) .'),
    },
    PRODUCT_NAME: {
      TRIPLES: ['?p ex:isSubjectOf ?pn .'],
      FILTERS: _.template('FILTER(regex(str(?pn), "<%= productName %>")) .'),
    },
    START_TIME: {
      TRIPLES: ['?p ex:hasStartTime ?st .'],
      FILTERS: _.template('FILTER(?st = "<%= startTime %>"^^xsd:datetime) .'),

    },
    END_TIME: {
      TRIPLES: ['?p ex:hasEndTime ?et .'],
      FILTERS: _.template('FILTER(?et = "<%= endTime %>"^^xsd:datetime) .'),
    },
  },
};
