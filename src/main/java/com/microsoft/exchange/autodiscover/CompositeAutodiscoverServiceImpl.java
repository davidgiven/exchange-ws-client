package com.microsoft.exchange.autodiscover;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.googlecode.ehcache.annotations.Cacheable;
import com.microsoft.exchange.exception.AutodiscoverException;

/**
 * An autodiscover implementation that queries all potential SOAP and POX
 * autodiscover endpoints for a given email address
 * 
 * @see <a href="http://msdn.microsoft.com/EN-US/library/office/ee332364(v=exchg.140).aspx">Implementing an Autodiscover Client in Microsoft Exchange</a>
 * @see <a href="http://msdn.microsoft.com/en-us/library/office/dn528374(v=exchg.150).aspx">When to use autodiscover</a>
 * 
 * @author ctcudd
 *
 */
public class CompositeAutodiscoverServiceImpl implements ExchangeAutodiscoverService {
	protected final Log log = LogFactory.getLog(this.getClass());
	
	@Autowired
	private Collection<ExchangeAutodiscoverService> autodiscoverServices;

	@Override
	@Cacheable(cacheName="autodiscoverCache")
	public String getAutodiscoverEndpoint(String email) throws AutodiscoverException {
		String ewsUrl = null;
		for(ExchangeAutodiscoverService service : getAutodiscoverServices()){
			try {
				ewsUrl = service.getAutodiscoverEndpoint(email);
			} catch (AutodiscoverException e) {
				log.warn("autodiscover failure: "+e.getMessage());
			}
			if(StringUtils.isNotBlank(ewsUrl)) return ewsUrl;
		}
		throw new AutodiscoverException("autodiscover failed.  cannot find ewsurl for email="+email);
	}

	@Override
	public List<String> getPotentialAutodiscoverEndpoints(String email) {
		List<String> potentials = new ArrayList<String>();
		for(ExchangeAutodiscoverService service : getAutodiscoverServices()){
			potentials.addAll(service.getPotentialAutodiscoverEndpoints(email));
		}
		return potentials;
	}

	public Collection<ExchangeAutodiscoverService> getAutodiscoverServices() {
		return autodiscoverServices;
	}

	public void setAutodiscoverServices(Collection<ExchangeAutodiscoverService> autodiscoverServices) {
		this.autodiscoverServices = autodiscoverServices;
	}

}
