package org.opendevup.restcontroller;

import java.util.Collection;
import org.opendevup.clients.IntegrationClient;
import org.opendevup.entities.Diplome;
import org.opendevup.entities.Enseignant;
import org.opendevup.entities.Service;
import org.opendevup.entities.proxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;




@Configuration
class Myconfiguration{
	@LoadBalanced
	@Bean
	public RestTemplate restTemplate() {
	    return new RestTemplate();
	}

}

@CrossOrigin
@RestController
public class ProxyGatewayRestService {

	@RequestMapping(value = "/",method=RequestMethod.GET)
	public String available() {
		return "Welcome To Proxy Service!";
	}
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private IntegrationClient integrationClient;
	
	/***************** AFFICHAGE *****************/
	@GetMapping("/diplomes")
	public Collection<Diplome> listDiplomes(){
		ParameterizedTypeReference<Resources<Diplome>> responseType= new ParameterizedTypeReference<Resources<Diplome>>() { };
		return restTemplate.exchange("http://diplome-service/diplomes", HttpMethod.GET, null,responseType).getBody().getContent();
	}
	
	
	@GetMapping("/enseignants")
	public Collection<Enseignant> listEnseignants(){
		ParameterizedTypeReference<Resources<Enseignant>> responseType= new ParameterizedTypeReference<Resources<Enseignant>>() { };
		return restTemplate.exchange("http://enseignant-service/enseignants", HttpMethod.GET, null,responseType).getBody().getContent();
	}
	
	@GetMapping("/services")
	public Collection<Service> listServices(){
		ParameterizedTypeReference<Resources<Service>> responseType= new ParameterizedTypeReference<Resources<Service>>() { };
		return restTemplate.exchange("http://services-service/services", HttpMethod.GET, null,responseType).getBody().getContent();
	}

	@GetMapping("/diplome-service/diplomeByidEnseignant/{idEnseignant}")
	public void diplomeByidEnseignant(@PathVariable Long idEnseignant) {
		
	}
	
	@GetMapping("/services-service/serviceByEnseignant/{idEnseignant}")
	public void serviceByEnseignant(@PathVariable Long idEnseignant) {
		
	}
	@GetMapping("/services-service/serviceByDiplome/{idDiplome}")
	public void serviceByDiplome(@PathVariable String idDiplome) {
		
	}
	//DRIBLE
	@GetMapping("diplomesProxy/{idDiplome}")
	Collection<Diplome> getDiplomeByUeId(@PathVariable String idDiplome) { 
	   return this.integrationClient.getDips(idDiplome);
	}
	
	//DRIBLE
	@GetMapping("servicesProxy/{ueId}")
	Collection<Service> getServicesByUeId(@PathVariable String ueId) { 
	   return this.integrationClient.getService(ueId);
	}
	
	/**************** AJOUTER DIPLOME & SERVICE****************************/
	@PostMapping("/addDiplomeProxy")
	public void addDiplome(@RequestBody Diplome dip) {
		HttpEntity<Diplome> responseType = (HttpEntity<Diplome>) new HttpEntity<>(dip);
		restTemplate.exchange("http://diplome-service/addDiplome", HttpMethod.POST, responseType, Diplome.class);
	}
	
	@PostMapping("/addServiceProxy")
	public void addService(@RequestBody Service service) {
		HttpEntity<Service> responseType = (HttpEntity<Service>) new HttpEntity<>(service);
		restTemplate.exchange("http://services-service/addService", HttpMethod.POST, responseType, Service.class);
	}
	
	/**************** SUPPRIMER DIPLOME****************************/
	@DeleteMapping("/deleteDiplomesProxy/{idDiplome}")
	public void deleteDiplome(@PathVariable String idDiplome) {
		restTemplate.delete("http://diplome-service/deleteDiplomes/{idDiplome}",idDiplome);
	}
	
	@DeleteMapping("/deleteServicesProxy/{ueId}")
	public void deleteService(@PathVariable String ueId) {
		restTemplate.delete("http://services-service/deleteServices/{ueId}",ueId);
	}
	
	/**************** PUT DIPLOME****************************/
	@PutMapping("/diplomesProxy/info")
	public @ResponseBody String updateDiplome(@RequestBody Diplome diplome){
	 restTemplate.put("http://diplome-servic/diplomes/info", diplome);
	 return "ok";
	}
	
	@PutMapping("/servicesProxy/info")
	public @ResponseBody String updateService(@RequestBody Service service){
	 restTemplate.put("http://services-servic/services/info", service);
	 return "ok";
	}

}

