package hr.fer.zemris.fuzzy.controller;

import hr.fer.zemris.fuzzy.sets.*;

import java.util.HashMap;

/**
 * Created by ivan on 11/9/15.
 */
public class FuzzyRule {
    private IDomain mOutputDomain;
    private HashMap<DomainElement, IFuzzySet> mRules;

    public FuzzyRule(IDomain outputDomain,HashMap<DomainElement,IFuzzySet> rules) {
        mOutputDomain = outputDomain;

        mRules = rules;

    }
//
//    public IFuzzySet infer(IFuzzySet inputA, IFuzzySet inputB){
//
//        IDomain domain = input.getDomain();
//
//        IFuzzySet output =new MutableFuzzySet(mOutputDomain);
//        for(DomainElement element: domain){
//            IFuzzySet set = Operations.unaryOperation(mRules.get(element), domainValue -> Operations.zadehAnd().valueAt(domainValue, input.getValueAt(element)));
//
//           // mRules.get(element)
//            input.getValueAt(element);
//        }
//
//        return null;
//    }
//

}
