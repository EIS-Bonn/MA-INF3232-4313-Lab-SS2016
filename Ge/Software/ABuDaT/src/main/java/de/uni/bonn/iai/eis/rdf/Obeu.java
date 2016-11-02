package de.uni.bonn.iai.eis.rdf;

import org.openrdf.model.IRI;
import org.openrdf.model.impl.SimpleValueFactory;

public final class Obeu {
    private static final SimpleValueFactory valueFactory = SimpleValueFactory.getInstance();

    public static final class Attribute {
        public static final String PREFIX = "obeu-attribute";
        public static final String NAMESPACE = "http://data.openbudgets.eu/ontology/dsd/attribute/";

        public static final IRI CURRENCY = valueFactory.createIRI(NAMESPACE, "currency");
        public static final IRI TAXES_INCLUDED = valueFactory.createIRI(NAMESPACE, "taxesIncluded");

        public static final class Currency {
            public static final String PREFIX = "obeu-currency";
            public static final String NAMESPACE = "http://data.openbudgets.eu/resource/codelist/currency";
        }
    }

    public static final class Dimension {
        public static final String PREFIX = "obeu-dimension";
        public static final String NAMESPACE = "http://data.openbudgets.eu/ontology/dsd/dimension/";

        public static final IRI BUDGETARY_UNIT = valueFactory.createIRI(NAMESPACE, "budgetaryUnit");
        public static final IRI FISCAL_PERIOD = valueFactory.createIRI(NAMESPACE, "fiscalPeriod");
        public static final IRI OPERATION_CHARACTER = valueFactory.createIRI(NAMESPACE, "operationCharacter");
        public static final IRI BUDGET_PHASE = valueFactory.createIRI(NAMESPACE, "budgetPhase");

        public static final class BudgetPhase {
            public static final String PREFIX = "obeu-budgetphase";
            public static final String NAMESPACE = "http://data.openbudgets.eu/resource/codelist/budget-phase/";

            public static final IRI DRAFT = valueFactory.createIRI(NAMESPACE, "Draft");
            public static final IRI REVISED = valueFactory.createIRI(NAMESPACE, "Revised");
            public static final IRI APPROVED = valueFactory.createIRI(NAMESPACE, "Approved");
            public static final IRI EXECUTED = valueFactory.createIRI(NAMESPACE, "Executed");
        }
    }

    public static final class Operation {
        public static final String PREFIX = "obeu-operation";
        public static final String NAMESPACE = "http://data.openbudgets.eu/resource/codelist/operation-character/";

        public static final IRI EXPENDITURE = valueFactory.createIRI(NAMESPACE, "Expenditure");
        public static final IRI REVENUE = valueFactory.createIRI(NAMESPACE, "Revenue");
    }

    public static final class Measure {
        public static final String PREFIX = "obeu-measure";
        public static final String NAMESPACE = "http://data.openbudgets.eu/ontology/dsd/measure/";

        public static final IRI AMOUNT = valueFactory.createIRI(NAMESPACE, "amount");
    }

    public static final class Classification {
        public static final IRI FUNCTIONAL_CLASSIFICATION =
                valueFactory.createIRI(Dimension.NAMESPACE, "functionalClassification");

        public static final IRI PROGRAMME_CLASSIFICATION =
                valueFactory.createIRI(Dimension.NAMESPACE, "programmeClassification");

        public static final IRI ECONOMIC_CLASSIFICATION =
                valueFactory.createIRI(Dimension.NAMESPACE, "economicClassification");

        public static final IRI ADMINISTRATIVE_CLASSIFICATION =
                valueFactory.createIRI(Dimension.NAMESPACE, "administrativeClassification");

        public static final IRI CLASSIFICATION =
                valueFactory.createIRI(Dimension.NAMESPACE, "classification");
    }

}
