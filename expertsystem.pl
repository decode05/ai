show(tmkoc, hindi, subTV, comedy, community, 5, 4500).
show(cid, hindi, sonytv, crime, mystery, 4.4, 1500).
show(crime_petrol, hindi, sonytv, crime, documentary, 4, 1500).
show(bigg_boss_hindi, hindi, colours_hindi, reality, entertainment, 3.9, 100).
show(kapil_sharma_show, hindi, sonytv, comedy, talk_show, 4.6, 3000).
show(chala_hawa_yeu_dya, marathi, zee_marathi, comedy, entertainment, 4.7, 3000).
show(maharashtrachi_hasya_jatra, marathi, sony_marathi, comedy, stand_up_comedy, 4.8, 3000).
show(fu_bai_fu, marathi, zee_marathi, comedy, entertainment, 3.9, 2500).
show(big_boss_marathi, marathi, colours_marathi, reality, entertainment, 3.9, 100).
show(naagin, hindi, colours_hindi, fantasy, supernatural, 3.9, 1500).
show(yeh_rishta_kya_kehlata_hai, hindi, starplus, drama, romance, 4, 3000).
show(bhabhi_ji_ghar_per_hai, hindi, andtv, drama, entertainment, 4.2, 1500).
show(fir, hindi, subTV, crime, entertainment, 3.3, 1200).
show(indian_idol, hindi, sonytv, reality, entertainment, 4.4, 500).

identify_show(Language, TV_Channel, Type, Theme, Rating, Total_episodes, Show) :-
    show(Show, Language, TV_Channel, Type, Theme, Rating, Total_episodes).

start :-
    write('Welcome to the SHOW Identifier!'), nl,
    write('Please describe the show you want to identify.'), nl,
    write('Which is the Language of the Show? (hindi/marathi) '), read(Language), nl,
    write('Which is the TV Channel of the show (subTV, sonytv, zee_marathi, sony_marathi, andtv, colours_hindi, colours_marathi, starplus)? '), read(TV_Channel), nl,
    write('Which type of show? (reality, comedy, crime, drama, fantasy, ?)'), read(Type), nl,
    write('What is the theme of the show? (community, documentary, talk_show, mystery, entertainment, romance, supernatural, stand_up_comedy)? '), read(Theme), nl,
    write('Rating of the show? '), read(Rating), nl,
    write('Total episodes in the show? (500/1200/1500/3000/100/2500/4500) '), read(Total_episodes), nl,
    identify_show(Language, TV_Channel, Type, Theme, Rating, Total_episodes, Show),
    (
        Show \= [] -> write('Based on your description, the show may be a '), write(Show), nl
        ; write('No show identified based on your given information'), nl
    ).