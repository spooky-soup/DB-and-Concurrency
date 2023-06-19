SELECT DISTINCT city
    FROM airports 
    INNER JOIN 
        (
            SELECT departure_airport AS airport_code, COUNT(departure_airport) AS departure_times 
            FROM 
                flights AS f
                INNER JOIN airports_data ON departure_airport = airport_code
            GROUP BY departure_airport
			UNION
			SELECT arrival_airport AS airport_code, COUNT(arrival_airport) AS arrival_times 
            FROM 
                flights AS f
                INNER JOIN airports_data ON arrival_airport = airport_code
            GROUP BY arrival_airport
        )
    AS cities
    USING(airport_code)