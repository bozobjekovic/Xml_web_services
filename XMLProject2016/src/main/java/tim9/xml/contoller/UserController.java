package tim9.xml.contoller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import tim9.xml.DTO.LoginDTO;
import tim9.xml.model.korisnik.Korisnik;
import tim9.xml.services.UserService;

@Controller
@RequestMapping(value="xmlWS/user")
public class UserController {
	
	@Autowired
	UserService userService;
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseEntity<Korisnik> login(@RequestBody LoginDTO loginDTO) {
		Korisnik korisnik = userService.findByEmail(loginDTO.getEmail());
		
		if (korisnik == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		if (!korisnik.getLozinka().equals(loginDTO.getPassword())) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<Korisnik>(korisnik, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/registration", method = RequestMethod.POST)
	public ResponseEntity<Korisnik> registration(@RequestBody Korisnik korisnik) {
		if (korisnik.getIme() == null || korisnik.getIme().trim().equals("") || 
			korisnik.getPrezime() == null || korisnik.getPrezime().trim().equals("") || 
			korisnik.getEmail() == null || korisnik.getEmail().trim().equals("") ||
			korisnik.getLozinka() == null || korisnik.getLozinka().trim().equals("")) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		Korisnik exKorisnik = userService.findByEmail(korisnik.getEmail());
		
		if (exKorisnik != null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		korisnik.setUloga("odbornik");
		userService.save(korisnik);
		
		return new ResponseEntity<Korisnik>(korisnik, HttpStatus.CREATED);
	}

}
